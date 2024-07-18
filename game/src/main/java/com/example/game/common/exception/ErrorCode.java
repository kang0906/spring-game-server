package com.example.game.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "Username is Duplicated", "사용 불가능한 아이디 입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "Bad Request", "잘못된 요청입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "잘못된 요청입니다."),
    NEED_INFRA(HttpStatus.BAD_REQUEST, "request need facility", "기반시설이 필요한 요청입니다."),
    ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "Command already in progress", "이미 수행중인 명령입니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "Data Not Found", "존재하지 않는 데이터 입니다."),
    CANT_EDIT(HttpStatus.BAD_REQUEST, "CUSTOMER_GROUP_01","수정 할 권한이 없습니다."),
    DESTINATION_NOT_EMPTY(HttpStatus.BAD_REQUEST, "Destination Field is Not Empty","목적지가 비어있지 않습니다."),
    NOT_ENOUGH_ITEM(HttpStatus.BAD_REQUEST,"Not enough item","아이템이 충분하지 않습니다."),
    CAN_NOT_USE_NEGATIVE_NUMBER(HttpStatus.BAD_REQUEST,"can not user negative number","0 이상만 사용할 수 있습니다."),
    NOT_VALID_USERNAME(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_04","이름이 유효하지 않습니다."),
    OUT_OF_RANGE(HttpStatus.BAD_REQUEST,"out of range","범위를 초과했습니다."),
    OUT_OF_MAP_RANGE(HttpStatus.BAD_REQUEST,"out of map range","맵 범위를 초과했습니다."),
    FRIENDLY_FIRE_NOT_ALLOWED(HttpStatus.BAD_REQUEST,"friendly fire is not allowed","아군은 공격할 수 없습니다."),
    NEED_COOL_DOWN_ERROR(HttpStatus.BAD_REQUEST,"need cool down","쿨타임이 지나지 않았습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "USER_01", "존재하는 이메일 입니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "USER_02", "토큰 유효시간이 지났습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "USER_03", "유효하지 않는 토큰입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "USER_04","아이디 또는 비밀번호가 유효하지 않습니다."),
    RETRY_EMAIL_CERTIFICATION(HttpStatus.BAD_REQUEST,"EMAIL_01","인증을 재시도 해주세요."),
    EMAIL_CERTIFICATION_EXPIRED(HttpStatus.BAD_REQUEST,"EMAIL_02","인증 시간이 초과되었습니다. 재시도 해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error","내부 서버 에러 발생"),
    ;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private String code;
    private String message;
}
