package com.example.mindtraining

class Operation (val n1: Int, val n2: Int, val op: Char) {

    public fun result(): Int {
        when(op) {
            '+' -> return n1 + n2
            '-' -> return n1 - n2
            '*' -> return n1 * n2
            '/' -> return n1 / n2
            else -> return 0
        }
    }

    override fun toString(): String {
        return "$n1 $op $n2"
    }
}