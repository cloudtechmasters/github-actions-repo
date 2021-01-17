package com.wkrzywiec.medium.noticeboard.util;

import com.wkrzywiec.medium.noticeboard.controller.dto.AuthorDTO;
import com.wkrzywiec.medium.noticeboard.controller.dto.BoardDTO;
import com.wkrzywiec.medium.noticeboard.controller.dto.NoticeDTO;
import com.wkrzywiec.medium.noticeboard.entity.Author;
import com.wkrzywiec.medium.noticeboard.entity.Board;
import com.wkrzywiec.medium.noticeboard.entity.Notice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TestDataFactory {

    public static Notice getSingleNotice(Long id){
        return Notice.builder()
                .id(id)
                .title("Notice " + id)
                .description("Notice description " + id)
                .build();
    }

    public static List<Notice> getNoticeList(Long noticesCount){
        return LongStream.rangeClosed(1, noticesCount)
                .mapToObj(TestDataFactory::getSingleNotice)
                .collect(Collectors.toList());
    }

    public static List<NoticeDTO> getNoticeListDTO(Long noticesCount) {
        return LongStream.rangeClosed(1, noticesCount)
                .mapToObj(TestDataFactory::getSingleNoticeDTO)
                .collect(Collectors.toList());
    }

    public static NoticeDTO getSingleNoticeDTO(Long id){
        return NoticeDTO.builder()
                .title("Notice " + id)
                .description("Notice description " + id)
                .build();
    }

    public static Board getSingleBoard(Long id, Long noticesCount){
        return Board.builder()
                .id(id)
                .title("Board " + id)
                .noticeList(getNoticeList(noticesCount))
                .build();
    }

    public static List<Board> getBoardList(Long boardsCount, Long noticesCount){
        return LongStream.rangeClosed(1, boardsCount)
                .mapToObj(id -> getSingleBoard(id, noticesCount))
                .collect(Collectors.toList());
    }

    public static BoardDTO getSingleBoardDTO(Long id, Long noticesCount){
        return BoardDTO.builder()
                .id(id)
                .title("Board " + id)
                .noticeList(getNoticeListDTO(noticesCount))
                .build();
    }

    public static List<BoardDTO> getBoardListDTO(Long boardsCount, Long noticesCount){
        return LongStream.rangeClosed(1, boardsCount)
                .mapToObj(id -> getSingleBoardDTO(id, noticesCount))
                .collect(Collectors.toList());
    }

    public static Author getSingleAuthor(Long id){
        return Author.builder()
                .id(id)
                .firstName("First Name " + id)
                .lastName("Last Name " + id)
                .build();
    }

    public static List<Author> getAuthorList(Long authorsCount){
        return LongStream.rangeClosed(1, authorsCount)
                .mapToObj(id -> getSingleAuthor(id))
                .collect(Collectors.toList());
    }

    public static AuthorDTO getSingleAuthorDTO(Long id){
        return AuthorDTO.builder()
                .id(id)
                .firstName("First Name " + id)
                .lastName("Last Name " + id)
                .build();
    }

    public static List<AuthorDTO> getAuthorListDTO(Long authorsCount){
        return LongStream.rangeClosed(1, authorsCount)
                .mapToObj(id -> getSingleAuthorDTO(id))
                .collect(Collectors.toList());
    }
}
