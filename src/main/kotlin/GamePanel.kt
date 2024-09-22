import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JPanel
import javax.swing.Timer

class GamePanel : JPanel(), ActionListener {
    val SCREEN_WIDTH = 600
    val SCREEN_HEIGHT = 600
    val UNIT_SIZE = 25
    val GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE
    val DELAY = 75

    val x = Array(GAME_UNITS) { 0 }
    val y = Array(GAME_UNITS) { 0 }

    var bodyParts = 6
    var applesEaten = 0

    var appleX = 0
    var appleY = 0

    var direction = 'R'
    var running = false
    var timer: Timer? = null

    init {
        this.preferredSize = Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)
        this.background = Color.BLACK
        this.isFocusable = true
        this.addKeyListener(MyKeyAdapter())
        startGame()
    }

    fun startGame() {
        newApple()
        this.running = true
        this.timer = Timer(DELAY, this)
        this.timer!!.start()
    }

    fun draw(g: Graphics) {
        if (running) {
            for (i in 0..<(SCREEN_HEIGHT / UNIT_SIZE)) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT)
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE)
            }

            g.color = Color.RED
            g.fillOval(this.appleX, this.appleY, UNIT_SIZE, UNIT_SIZE)

            for (i in 0..<bodyParts) {
                if (i == 0) {
                    g.color = Color.GREEN
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE)
                } else {
                    g.color = Color(45, 180, 0)
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE)
                }
            }

            g.color = Color.RED
            g.font = Font("Arial", Font.BOLD, 40)
            val metrics = getFontMetrics(g.font)
            g.drawString("Score: $applesEaten", (SCREEN_WIDTH - metrics.stringWidth("Score: $applesEaten")) / 2, g.font.size)
        } else {
            gameOver(g)
        }
    }

    fun newApple() {
        this.appleX = (0..<(SCREEN_WIDTH / UNIT_SIZE)).random() * UNIT_SIZE
        this.appleY = (0..<(SCREEN_HEIGHT / UNIT_SIZE)).random() * UNIT_SIZE
    }

    fun move() {
        for (i in bodyParts downTo 1) {
            x[i] = x[i - 1]
            y[i] = y[i - 1]
        }

        when (direction) {
            'U' -> y[0] = y[0] - UNIT_SIZE
            'D' -> y[0] = y[0] + UNIT_SIZE
            'L' -> x[0] = x[0] - UNIT_SIZE
            'R' -> x[0] = x[0] + UNIT_SIZE
        }
    }

    fun checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++
            applesEaten++
            newApple()
        }
    }

    fun checkCollisions() {
        for (i in bodyParts downTo 1) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                this.running = false
            }
        }

        if (x[0] < 0) {
            this.running = false
        }

        if (x[0] > SCREEN_WIDTH) {
            this.running = false
        }

        if (y[0] < 0) {
            this.running = false
        }

        if (y[0] > SCREEN_HEIGHT) {
            this.running = false
        }

        if (!this.running) {
            this.timer!!.stop()
        }
    }

    fun gameOver(g: Graphics) {
        g.color = Color.RED
        g.font = Font("Arial", Font.BOLD, 75)

        val metrics1 = getFontMetrics(g.font)

        g.drawString("Game Over!", (SCREEN_WIDTH - metrics1.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2)

        g.color = Color.RED
        g.font = Font("Arial", Font.BOLD, 40)
        val metrics2 = getFontMetrics(g.font)
        g.drawString("Score: $applesEaten", (SCREEN_WIDTH - metrics2.stringWidth("Score: $applesEaten")) / 2, g.font.size)
    }

    inner class MyKeyAdapter : KeyAdapter() {
        override fun keyPressed(e: KeyEvent?) {
            super.keyPressed(e)

            when (e!!.keyCode) {
                KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L'
                    }
                }
                KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R'
                    }
                }
                KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U'
                    }
                }
                KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D'
                    }
                }
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        draw(g)
    }

    override fun actionPerformed(p0: ActionEvent?) {
        if (this.running) {
            move()
            checkApple()
            checkCollisions()
        }
        repaint()
    }
}