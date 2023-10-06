package com.springboot.couchbase.springbootrealworld.exception

class AppException(val error: Error) : RuntimeException(error.message)
