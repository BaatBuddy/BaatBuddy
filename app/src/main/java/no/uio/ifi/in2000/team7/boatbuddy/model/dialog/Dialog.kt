package no.uio.ifi.in2000.team7.boatbuddy.model.dialog

sealed class Dialog {
    data object ShowStartDialog : Dialog()
    data object ShowFinishDialog : Dialog()
    data object ShowNoDialog : Dialog()
}