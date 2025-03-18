package com.ling.chat.tools

import org.springframework.ai.tool.annotation.Tool
import java.util.concurrent.ConcurrentHashMap

/**
 *@author LingLambda
 *@since 2025/3/10 16:54
 */
class BookTools {
    @Tool(description = "Get the list of books written by the given author available in the library")
    fun getBooksByAuthor(author: Author): BookList {
        return BookList(
            books.values.filter { book: Book -> author.name == book.author }
                .toList()
        )
    }

    data class Book(val title: String, val author: String)
    data class Author(val name: String)
    data class BookList(val list: List<Book>)

    companion object {
        private val books: MutableMap<Int, Book> = ConcurrentHashMap()

        init {
            books[1] = Book("His Dark Materials", "Philip Pullman")
            books[2] = Book("Narnia", "C.S. Lewis")
            books[3] = Book("The Hobbit", "J.R.R. Tolkien")
            books[4] = Book("The Lord of The Rings", "J.R.R. Tolkien")
            books[5] = Book("The Silmarillion", "J.R.R. Tolkien")
            books[6] = Book("I'm King", "J.R.R. Tolkien")
        }
    }
}