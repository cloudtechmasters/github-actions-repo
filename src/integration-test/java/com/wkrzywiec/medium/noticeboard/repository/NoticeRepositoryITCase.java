package com.wkrzywiec.medium.noticeboard.repository;

import com.wkrzywiec.medium.noticeboard.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Integration Tests of NoticeRepository with H2 Database")
public class NoticeRepositoryITCase {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Get a Notice by Id")
    public void givenSingleNotice_whenFindById_thenGetNotice() {
        //given

        entityManager.persist(singleNotice(11L));
        entityManager.flush();
        Notice savedNotice = getNoticeResultListSavedInDb().get(0);

        //when
        Optional<Notice> optionalNotice = noticeRepository.findById(savedNotice.getId());

        //then
        assertFalse(optionalNotice.isEmpty());
        assertEquals("Notice 11", optionalNotice.get().getTitle());
    }

    @Test
    @DisplayName("Get a list with 3 Notices")
    public void given3Notices_whenFindAll_thenGetNotices() {
        //given
        entityManager.persist(singleNotice(1L));
        entityManager.persist(singleNotice(2L));
        entityManager.persist(singleNotice(3L));
        entityManager.flush();

        //when
        Iterable<Notice> noticeIterable = noticeRepository.findAll();

        //then
        List<Notice> noticeList =
                StreamSupport.stream(noticeIterable.spliterator(), false)
                        .collect(Collectors.toList());

        assertFalse(noticeList.isEmpty());
        assertEquals(3, noticeList.size());
    }

    @Test
    @DisplayName("Get a Notice by Id when 2 Notices are in database")
    public void given2Notices_whenFindById_thenGetSingleNotice() {
        //given
        entityManager.persist(singleNotice(1L));
        entityManager.persist(singleNotice(2L));
        entityManager.flush();

        Notice savedNotice = getNoticeResultListSavedInDb().get(0);

        //when
        Optional<Notice> optNotice = noticeRepository.findById(savedNotice.getId());

        //then
        assertTrue(optNotice.isPresent());
        assertNotNull(optNotice.get().getId());
    }

    @Test
    @DisplayName("Save a Notice")
    public void givenSingleNotice_whenSave_thenNoticeIsSaved() {
        //given
        Notice notice = singleNotice(1L);

        //when
        noticeRepository.save(notice);

        //then
        Notice savedNotice = getNoticeResultListSavedInDb().get(0);
        assertNotNull(savedNotice.getId());
        assertEquals("Notice 1", savedNotice.getTitle());
    }

    @Test
    @DisplayName("Delete Notice by Id")
    public void given2SavedNotices_whenDeleteById_thenOnlyOneNoticeInDb() {
        //given
        entityManager.persist(singleNotice(1L));
        entityManager.persist(singleNotice(2L));
        entityManager.flush();

        Notice deletedNotice = getNoticeResultListSavedInDb().get(0);

        //when
        noticeRepository.deleteById(deletedNotice.getId());

        //then
        List<Notice> noticeList = getNoticeResultListSavedInDb();

        assertEquals(1, noticeList.size());
        assertFalse(deletedNotice.getId().equals(noticeList.get(0).getId()));
    }

    private Notice singleNotice(Long number){
        return Notice.builder()
                .title("Notice " + number)
                .description("Notice description " + number)
                .build();
    }

    private List<Notice> getNoticeResultListSavedInDb() {
        return entityManager
                .createNativeQuery("SELECT Notice.* FROM Notice", Notice.class)
                .getResultList();
    }
}
