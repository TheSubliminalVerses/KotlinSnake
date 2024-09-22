import javax.swing.JFrame

class MainWindow(private val windowName: String) : JFrame(windowName) {
    init {
        this.add(GamePanel())
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.isResizable = false
        this.pack()
        this.isVisible = true
        this.setLocationRelativeTo(null)
    }
}