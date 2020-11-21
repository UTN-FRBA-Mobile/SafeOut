package com.utn_frba_mobile_2020_c2.safeout.extensions

 fun byteArrayToHexString(inarray: ByteArray?): String? {
    inarray as ByteArray

    var i: Int
    var j: Int = 0
    var entrada: Int
    val hex =
        arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
    var out = ""

    while (j < inarray.size) {
        entrada = inarray[j].toInt() and 0xff
        i = entrada shr 4 and 0x0f
        out += hex[i]
        i = entrada and 0x0f
        out += hex[i]
        ++j
    }
    return out
}
 fun deHexadecimalAEntero(hexaDecimalN: String?): Long {

    if(checkHexaDecimalNumber(hexaDecimalN!!)) {

        var i         = hexaDecimalN.length - 1
        var decimalN: Long = 0
        var base : Long    = 1

        while(i >= 0) {
            val charAtPos = hexaDecimalN[i]
            val lastDigit = if((charAtPos >= '0') && (charAtPos <= '9')) {
                charAtPos - '0'
            } else if((charAtPos >= 'A') && (charAtPos <= 'F')) {
                charAtPos.toInt() - 55
            } else if((charAtPos >= 'a') && (charAtPos <= 'f')) {
                charAtPos.toInt() - 87
            } else {
                0
            }
            decimalN += lastDigit * base
            base *= 16
            i--
        }
        return decimalN
    } else {
        return 0
    }
}
 fun checkHexaDecimalNumber(hexaDecimalNum: String): Boolean {
    var isHexaDecimalNum = true

    for(charAtPos in hexaDecimalNum) {
        if(!(((charAtPos >= '0') && (charAtPos <= '9'))
                    || ((charAtPos >= 'A') && (charAtPos <= 'F'))
                    || ((charAtPos >= 'a') && (charAtPos <= 'f'))
                    )) {
            isHexaDecimalNum = false
            break
        }
    }
    return isHexaDecimalNum
}