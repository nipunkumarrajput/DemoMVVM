package com.nipun.demoapplication

/*
* Created by Nipun Kumar Rajput on 26-04-2020.
* Copyright (c) 2020 Nipun. All rights reserved.
*/
data class LoginFormState(
    val userEmailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPassError: Int? = null,
    val isDataValid: Boolean = false
)
