package com.konstantinmuzhik.hw1todoapp.ioc



/**
 * Container that stores all classes, that are Application-scoped,
 * i.e. for classes that live as long as application itself.
 *
 * This is implemented by storing the reference to the instance of this class in
 * [App.applicationComponent].
 */
class ApplicationComponent {
//    private val dataSource = HardCodedDataSource()
//    private val toDoItemsRepository = ToDoItemsRepository(dataSource)
//    private val authorRepository = AuthorRepository(dataSource)
//    private val articlesWithAuthorsUseCaseFactory = { // creates new UseCase instance each time
//        ArticlesWithAuthorsUseCase(articleRepository, authorRepository)
//    }
//
//    /**
//     * Public getter allows all other classes to use same instance from [ApplicationComponent].
//     */
//    val viewModelFactory = ViewModelFactory(articleRepository, articlesWithAuthorsUseCaseFactory)
}
