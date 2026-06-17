package config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import repository.BookRepository;
import repository.BorrowRepository;
import repository.DatabaseBookRepository;
import repository.DatabaseBorrowRepository;
import repository.DatabaseStudentRepository;
import repository.StudentRepository;
import service.LibraryManager;
import service.LibraryManagerImpl;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {

        bind(DatabaseBookRepository.class)
                .to(BookRepository.class);

        bind(DatabaseStudentRepository.class)
                .to(StudentRepository.class);

        bind(DatabaseBorrowRepository.class)
                .to(BorrowRepository.class);

        bind(LibraryManagerImpl.class)
                .to(LibraryManager.class);
    }
}
