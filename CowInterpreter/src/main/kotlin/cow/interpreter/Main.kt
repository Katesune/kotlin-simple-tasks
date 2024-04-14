package cow.interpreter

import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.File

private val moo_dict = listOf("MoO", "MOo", "moO", "mOo", "moo", "MOO", "OOM", "oom", "mOO", "Moo", "OOO")

private const val file_name = "data/fib.cow"

// MOO - если значение = 0 пропустить 1 команду и дойти до команды после moo,
// если значение != 0 продолжить выполнение по порядку

// moo - идем в обратном порядке, пропуская одну команду перед moo

class Interpreter(private val commands: List<String>) {
    private val buffer = Array<Char> (100) { _ -> 0.toChar()}
    private var ptr = 0

    private var stateCommand = true
    private var index = 0
    private var index_acc = 1

    fun startInterpret() {
        while (index < commands.size) {
            require(index >= 0 ) {
                "The index must be at least 0."
            }

            val command = commands[index]

            if (stateCommand) {
                interpret(command)
            } else {
                if (index_acc == 1 && command == moo_dict[4]) { // 4 - found moo, changing state for command after moo
                    stateCommand = true
                }
                if (index_acc == -1 && command == moo_dict[5]) { // 5 - found MOO, check value !0, if !0 change navigation forward
                    stateCommand = true // if value != 0 continue program, if value = 0 start program after moo
                    index_acc = 1
                    interpret(command)
                }
            }

            index += index_acc
        }
    }

    private fun interpret(command: String) {
        when (command) {
            moo_dict[0] -> increase()
            moo_dict[1] -> reduce()
            moo_dict[2] -> increaseCellNumber()
            moo_dict[3] -> reduceCellNumber()
            moo_dict[4] -> returnToCycleEnd()
            moo_dict[5] -> {
                if (buffer[ptr] == (0).toChar())
                { goToCycleStart() }
            }
            moo_dict[6] -> printCellValue()
            moo_dict[7] -> readValue()
            moo_dict[8] -> readCommand()
            moo_dict[9] -> getValueOrInput()
            moo_dict[10] -> resetCell()
        }
    }

    private fun increase() {
        buffer[ptr] ++
    }

    private fun reduce() {
        buffer[ptr] --
    }

    private fun increaseCellNumber() {
        ptr ++
    }

    private fun reduceCellNumber() {
        ptr --
        require(ptr >= 0) {
            "The ptr must be at least 0."
        }
    }

    private fun returnToCycleEnd() {
        // starting the search for a paired 'MOO', commands are not executed during the search, starting with 'MOO'
        index--
        index_acc = -1
        stateCommand = false
    }

    private fun goToCycleStart() {
        // starting the search for a paired 'moo', commands are not executed during the search, starting after 'moo'
        index ++
        index_acc = 1
        stateCommand = false
    }

    private fun printCellValue() {
        println(buffer[ptr].code)
    }

    private fun resetCell() {
        buffer[ptr] = (0).toChar()
    }

    private fun readValue() {
        buffer[ptr] = readlnOrNull()?.toCharArray()?.getOrNull(0) ?:  buffer[ptr]
    }

    private fun readCommand() {
        val command = readlnOrNull()
        if (command != null && command.toString() != moo_dict[8]) {
            interpret(command)
        }

    }

    private fun getValueOrInput() {
        if (buffer[ptr] == (0).toChar()) {
            readValue()
        } else { print(buffer[ptr]) }
    }
}

fun main() {
    println("Start")

    val commands = File(file_name)
        .readText()
        .replace("\n", " ")
        .split(" ")

    val interpreter = Interpreter(commands)
    interpreter.startInterpret()

    println(" Done. ")
}