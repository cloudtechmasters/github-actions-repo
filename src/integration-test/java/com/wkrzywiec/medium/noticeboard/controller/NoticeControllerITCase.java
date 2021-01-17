package com.wkrzywiec.medium.noticeboard.controller;

import com.google.gson.JsonObject;
import com.wkrzywiec.medium.noticeboard.config.TestDataProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@DisplayName("Integration Tests of the NoticeController REST endpoints")
public class NoticeControllerITCase extends CrudControllerITCase {

    @Test
    @DisplayName("GET a list with 4 Notices")
    public void whenGETAllNotices_thenGetListOf4Notices() {
        //when
        ValidatableResponse response = given()
                .when()
                .get( baseURL + "/notices/")

                .prettyPeek()
                .then();

        //then
        response.statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
    @DisplayName("GET a Notice by Id")
    public void givenNoticeId_thenGetSingleNotice() {
        //given
        Long noticeId = 1L;

        //when
        ValidatableResponse response = given()
                .when()
                .get(baseURL + "/notices/" + noticeId)

                .prettyPeek()
                .then();

        //then
        response.statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("id", equalTo(1))
                .body("title", equalTo("Notice 1 title"))
                .body("description", equalTo("Notice 1 description"))
                .body("author.id", equalTo(1))
                .body("author.firstName", equalTo("John"))
                .body("author.lastName", equalTo("Doe"));
    }

    @Test
    @DisplayName("POST a Notice to create it")
    public void givenNotice_whenPOSTSave_thenGetSavedNotice(){
        //given
        JsonObject noticeJson = TestDataProvider.getNoticeJson();

        //when
        ValidatableResponse response = given()
                .contentType("application/json")
                .body(noticeJson.toString())

                .when()
                .post(baseURL + "/notices/")

                .prettyPeek()
                .then();

        //then
        response.statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
//                .body("creationDate", notNullValue())
                .body("title", equalTo("Notice TEST title"))
                .body("description", equalTo("Notice TEST description"));
    }

    @Test
    @DisplayName("DELETE a Notice by Id")
    public void givenNoticeId_whenDELETENotice_thenNoticeIsDeleted() {
        //given
        Long noticeId = 3L;

        //when
        ValidatableResponse response = given()
                .contentType("application/json")

                .when()
                .delete(baseURL + "/notices/" + noticeId)

                .prettyPeek()
                .then();

        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("PUT a Notice by Id to update it")
    public void givenIdAndUpdatedNotice_whenPUTUpdate_thenNoticeIsUpdated() {
        //given
        Long noticeId = 4L;
        JsonObject noticeJson = TestDataProvider.getNoticeJson();

        //when
        ValidatableResponse response = given()
                .contentType("application/json")
                .body(noticeJson.toString())

                .when()
                .put(baseURL + "/notices/" + noticeId)

                .prettyPeek()
                .then();

        response.statusCode(HttpStatus.OK.value());
    }
}
