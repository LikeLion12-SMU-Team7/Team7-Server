package com.example.alcohol_free_day.global.common.code;

import com.example.alcohol_free_day.global.common.code.ErrorReasonDTO;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();

}