package com.ling.manager.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ling.common.exception.ChunkTextException;
import io.swagger.v3.oas.annotations.servers.Server;
import java.io.IOException;
import java.util.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author LingLambda
 * @date 2025/1/414:12
 */
@Server
public class ChunkUtil {

  private static final Logger log = LoggerFactory.getLogger(ChunkUtil.class);

  /**
   * @param file 需要切割的文本文件，支持markdown，txt等纯文本格式
   */
  public static List<Document> textChunks(MultipartFile file) throws IOException, ChunkTextException {
    try {
      String stringText = Arrays.toString(file.getBytes());
      return textChunks(stringText);
    } catch (IOException e) {
      log.error("ToStringError:{}", e.getMessage());
      throw e;
    }
  }

  /**
   * @param stringText 需要切割的文本
   */
  public static List<Document> textChunks(String stringText) throws ChunkTextException {
    List<String> strings = chunkText(stringText, 300, 1000);
    List<Document> documentList = new ArrayList<>();
    for (String string : strings) {
      documentList.add(Document.builder().id(UUID.randomUUID().toString()).text(string).build());
    }
    return documentList;
  }

  /**
   * 将pdf分割为向量存储数据类型
   *
   * @param file 切割的pdf文件
   */
  public static List<Document> pdfChunks(MultipartFile file)
      throws ChunkTextException, IOException {
    return pdfChunksByCount(file, 300, 1000);
  }

  /**
   * 解析pdf为向量存储数据类型
   *
   * @param file 切割的pdf文件
   * @param minCount 最小长度
   * @param bigCount 过大长度
   */
  public static List<Document> pdfChunksByCount(MultipartFile file, int minCount, int bigCount)
      throws IOException, ChunkTextException {

    try (PDDocument pdf = Loader.loadPDF(file.getBytes())) {
      PDFTextStripper pdfTextStripper = new PDFTextStripper();
      String itemPage = pdfTextStripper.getText(pdf);
      List<String> strings = chunkText(itemPage, minCount, bigCount);
      List<Document> documentList = new ArrayList<>();
      int idSuffix = 0;
      for (String string : strings) {
        documentList.add(Document.builder().id(getFileNameWithoutExtension(file.getOriginalFilename())+idSuffix).text(string).build());
        idSuffix++;
      }
      return documentList;
    } catch (IOException e) {
      log.error("PdfIOError:{}", e.getMessage());
      throw e;
    }
  }

  // 函数：从后往前查找 `.` 并返回 `.` 之前的部分
  private static String getFileNameWithoutExtension(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return fileName;  // 如果文件名为空，直接返回空
    }

    // 查找最后一个 `.` 的位置
    int dotIndex = fileName.lastIndexOf('.');

    // 如果没有找到 `.`，则返回整个文件名
    if (dotIndex == -1) {
      return fileName;
    }

    // 截取 `.` 前面的部分
    return fileName.substring(0, dotIndex);
  }

  /**
   * 解析Json为向量存储数据类型，没有content或text的字段会被忽略
   *
   * @param file file文件
   */
  public static List<Document> jsonChunks(MultipartFile file) throws ChunkTextException, IOException {
    return jsonChunks(file, 1000);
  }

  /**
   * 解析Json为向量存储数据类型，没有content或text的字段会被忽略
   *
   * @param file file文件
   * @param bigCount 过大长度
   */
  public static List<Document> jsonChunks(MultipartFile file, int bigCount)
      throws ChunkTextException, IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    // 解析 JSON 字符串为 JsonNode
    JsonNode jn = null;
    try {
      jn = objectMapper.readTree(file.getInputStream());
    } catch (IOException e) {
      log.error("jsonIOError:{}", e.getMessage());
      throw e;
    }
    List<Document> documentList = new ArrayList<>();
    if (jn.isArray()) {
      Iterator<JsonNode> jnEl = jn.elements();
      while (jnEl.hasNext()) {
        Iterator<Map.Entry<String, JsonNode>> fields = jnEl.next().fields();
        List<Document> ds = jsonItemChunk(fields, file.getOriginalFilename(), bigCount);
        if(null != ds){
          documentList.addAll(ds);
        }
      }
    } else {
      documentList = jsonItemChunk(jn.fields(), file.getOriginalFilename(), bigCount);
    }
    System.out.println("documentList = " + documentList);
    return documentList;
  }

  private static List<Document> jsonItemChunk(
      Iterator<Map.Entry<String, JsonNode>> item, String fileName, int bigCount)
      throws ChunkTextException {
    List<Document> documentList = new ArrayList<>();
    Document.Builder builder = Document.builder().id(fileName).text("");
    while (item.hasNext()){
      Map.Entry<String, JsonNode> s = item.next();
      JsonNode value = s.getValue();
      if (value.isObject()) {
        continue;
      }
      if ("id".equals(s.getKey()) || "title".equals(s.getKey())) {
        builder.id(value.asText());
      } else if ("content".equals(s.getKey()) || "text".equals(s.getKey())) {
        builder.text(value.asText());
      } else {
        builder.metadata(s.getKey(), value.asText());
      }
    }
    String text = builder.build().getText();
    if(text.isEmpty()){
      //如果text为空，直接跳出
      return null;
    }
    // 处理正文过长
    if (text.length() > bigCount) {
      log.debug("text to long, length is " + text.length() + ", chunking");
      List<String> strings = chunkText(text, 0, bigCount);
      for (String string : strings) {
        log.debug("chunk add");
        documentList.add(builder.text(string).build());
      }
    } else {
      documentList.add(builder.build());
    }

    return documentList;
  }

  /**
   * 切分过大的字符串
   *
   * @param s 输入字符串
   * @param minCount 最小长度
   * @param bigCount 过大长度
   * @throws ChunkTextException 切分异常
   */
  private static List<String> chunkText(String s, int minCount, int bigCount)
      throws ChunkTextException {
    try {
      Set<Character> cutChars = Set.of('.', '。', '!', '！', ';', '；');
      List<String> chunks = new ArrayList<>();
      int oldIndex = 0;
      int nowIndex = 0;
      final int MAX_COUNT = 1600; // 最大长度，当字符串超过这个阈值会直接切分
      char previousChar = ' ';
      for (char c : s.toCharArray()) {
        boolean isCutChar = cutChars.contains(previousChar);
        boolean isNewParagraph = isCutChar && (c == '\r' || c == '\n');
        boolean isMinCount = nowIndex - oldIndex > minCount;
        boolean isBigCount = nowIndex - oldIndex > bigCount;
        boolean isMaxCount = nowIndex - oldIndex > MAX_COUNT;
        if (isMinCount && (isNewParagraph || isBigCount && isCutChar) || isMaxCount) {
          chunks.add(s.substring(oldIndex, nowIndex));
          oldIndex = nowIndex;
        }
        previousChar = c;
        nowIndex++;
      }
      if (oldIndex != nowIndex) {
        chunks.add(s.substring(oldIndex, nowIndex));
      }
      return chunks;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ChunkTextException(e.getMessage());
    }
  }
}
