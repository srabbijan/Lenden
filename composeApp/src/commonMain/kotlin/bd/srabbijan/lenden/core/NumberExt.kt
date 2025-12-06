package bd.srabbijan.lenden.core



fun Double?.toCurrencyFormat(): String {
    val amount = this ?: 0.0

    val isWhole = amount % 1.0 == 0.0

    val safeString = if (isWhole) {
        amount.toLong().toString()
    } else {
        roundToTwoDecimal(amount)
    }

    val formatted = formatWithIndianGrouping(safeString)
    return "$formatted ৳"
}

fun roundToTwoDecimal(value: Double): String {
    val rounded = (value * 100).toLong() / 100.0
    return rounded.toString()
}

fun formatWithIndianGrouping(number: String): String {
    val parts = number.split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) "." + parts[1] else ""

    val lastThree = integerPart.takeLast(3)
    var remaining = integerPart.dropLast(3)

    val builder = StringBuilder()

    while (remaining.length > 2) {
        builder.insert(0, "," + remaining.takeLast(2))
        remaining = remaining.dropLast(2)
    }

    if (remaining.isNotEmpty()) {
        builder.insert(0, remaining)
    }

    return builder.toString() +
            (if (builder.isNotEmpty()) "," else "") +
            lastThree + decimalPart
}
fun Double?.toPositive(): Double {
    return this?.let { if (it < 0) it * -1 else it } ?: 0.0
}
fun Double?.toNegative(): Double {
    return this?.let { if (it < 0) it else it  * -1} ?: 0.0
}

fun Double?.isNegative(): Boolean{
    return this?.let { it < 0 } ?: false
}
fun Double?.isPositive(): Boolean{
    return this?.let { it > 0 } ?: false
}
