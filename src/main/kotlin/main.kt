import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater({
        run {
            MainWindow("Snake")
        }
    })
}