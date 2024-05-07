package no.uio.ifi.in2000.team7.boatbuddy.model.dialog

sealed class Dialog {
    object ShowStartDialog : Dialog()
    object ShowFinishDialog : Dialog()
    object ShowNoDialog : Dialog()
}