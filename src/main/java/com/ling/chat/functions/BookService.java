package com.ling.chat.functions;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private static final Map<Integer, Book> books = new ConcurrentHashMap<>();

  static {
    books.put(1, new Book("His Dark Materials", "Philip Pullman"));
    books.put(2, new Book("Narnia", "C.S. Lewis"));
    books.put(3, new Book("The Hobbit", "J.R.R. Tolkien"));
    books.put(4, new Book("The Lord of The Rings", "J.R.R. Tolkien"));
    books.put(5, new Book("The Silmarillion", "J.R.R. Tolkien"));
    books.put(6, new Book("I'm King", "J.R.R. Tolkien"));
  }

  public BookList getBooksByAuthor(Author author) {
    return new BookList(
        books.values().stream().filter(book -> author.name().equals(book.author())).toList());
  }

  public record Book(String title, String author) {}

  public record Author(String name) {}

  public record BookList(List<Book> list) {}
}
