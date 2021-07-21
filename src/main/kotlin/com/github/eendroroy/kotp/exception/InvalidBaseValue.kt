package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class InvalidBaseValue: RuntimeException("base was not in valid range 10..36")
