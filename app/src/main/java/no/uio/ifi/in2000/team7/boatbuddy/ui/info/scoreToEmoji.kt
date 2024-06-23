package no.uio.ifi.in2000.team7.boatbuddy.ui.info

fun scoreToEmoji(score: Double?): String? {
    return when (score) {
        null -> null
        in Double.NEGATIVE_INFINITY..0.0, in 101.0..Double.POSITIVE_INFINITY -> throw IllegalArgumentException(
            "Score must be between 0.0 and 100.0"
        )

        in 0.0..50.0 -> "\ud83d\ude2b"
        in 50.0..60.0 -> "\ud83d\ude14"
        in 60.0..70.0 -> "\ud83d\ude10"
        in 70.0..80.0 -> "\ud83d\ude0a"
        in 80.0..90.0 -> "\ud83d\ude03"
        in 90.0..99.0 -> "\ud83d\ude0d"
        else -> "\ud83d\ude0d\ud83d\udcaf"
    }
}