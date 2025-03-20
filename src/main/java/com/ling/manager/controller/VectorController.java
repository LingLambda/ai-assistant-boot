package com.ling.manager.controller;

import com.ling.common.entity.TextObj;
import com.ling.common.exception.ChunkTextException;
import com.ling.common.util.Result;
import com.ling.common.util.ResultCodeEnum;
import com.ling.manager.utils.ChunkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LingLambda
 * @since 2025/1/1下午4:16
 */
@RestController
@CrossOrigin
@RequestMapping("admin")
public class VectorController {

  private static final Logger log = LoggerFactory.getLogger(VectorController.class);
  private final VectorStore vectorStore;

  VectorController(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  @GetMapping("vector/query")
  public Result<?> vectorQuery(
      @RequestParam String message, @RequestParam("top_k") Integer topK) {
    try {
      if (topK == null) {
        topK = 10;
      }
      List<Document> documentList =
          this.vectorStore.similaritySearch(
              SearchRequest.builder()
                  .query(message)
                  .topK(topK)
                  .build());
      return Result.build(documentList, ResultCodeEnum.SUCCESS);
    } catch (Exception e) {
      log.error("未知错误", e);
      return Result.build("未知内部错误", ResultCodeEnum.FAIL);
    }
  }

  @PostMapping("vector/add")
  public Result<?> addData(@RequestBody MultipartFile[] file) {
    if (file == null || file.length == 0) {
      return Result.build("上传的文件列表为空", ResultCodeEnum.FAIL);
    }
    int count = 0;
    try {
      for (MultipartFile multipartFile : file) {
        List<Document> documentList = new ArrayList<>();
        String fileExtension = getFileExtension(multipartFile);
        if ("pdf".equals(fileExtension)) {
          documentList = ChunkUtil.pdfChunks(multipartFile);
        } else if ("json".equals(fileExtension)) {
          documentList = ChunkUtil.jsonChunks(multipartFile);
        }
        vectorStore.add(documentList);
        count++;
      }
      return Result.build("全部上传成功", ResultCodeEnum.SUCCESS);
    } catch (ChunkTextException e) {
      return Result.build(count + "个文件上传成功" + e.getMessage(), ResultCodeEnum.FILE_CHUNK_ERROR);
    } catch (IOException e) {
      return Result.build(count + "个文件上传成功" + e.getMessage(), ResultCodeEnum.FILE_IO_ERROR);
    } catch (Exception e) {
      log.error("{}个文件上传成功", count, e);
      return Result.build(count + "个文件上传成功" + "未知内部错误", ResultCodeEnum.FAIL);
    }
  }

  @PostMapping("vector/add_text")
  public Result<?> addData(@RequestBody TextObj textObj) {
    String stringText = textObj.getStringText();
    if (stringText == null || stringText.isEmpty()) {
      return Result.build("上传的数据为空", ResultCodeEnum.FAIL);
    }
    try {
      List<Document> documentList = ChunkUtil.textChunks(stringText);
      vectorStore.add(documentList);
      return Result.build("文本上传成功", ResultCodeEnum.SUCCESS);
    } catch (ChunkTextException e) {
      return Result.build("上传失败，切分错误" + e.getMessage(), ResultCodeEnum.FILE_CHUNK_ERROR);
    } catch (Exception e) {
      log.error("上传失败，未知错误", e);
      return Result.build("上传失败" + "未知内部错误", ResultCodeEnum.FAIL);
    }
  }

  @PostMapping("vector/delete")
  private Result<?> deleteDate(@RequestBody List<String> idList) {
    try {
      if (idList == null || idList.isEmpty()) {
        return Result.build("选择为空", ResultCodeEnum.FAIL);
      }
      vectorStore.delete(idList);
      //      if (!delete.orElse(false)) {
      //        return Result.build("id可能不存在", ResultCodeEnum.FILE_DELETE_ERROR);
      //      }
      return Result.build("删除成功", ResultCodeEnum.SUCCESS);
    } catch (Exception e) {
      log.error("未知错误", e);
      return Result.build("未知内部错误", ResultCodeEnum.FAIL);
    }
  }

  private String getFileExtension(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    if (fileName != null && fileName.contains(".")) {
      return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    return ""; // 如果没有扩展名，返回空字符串
  }
}
