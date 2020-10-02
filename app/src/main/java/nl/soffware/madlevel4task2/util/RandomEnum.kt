package nl.soffware.madlevel4task2.util

import java.util.Random

class RandomEnum<E : Enum<E>>(token: Class<E>) {
    private val values: Array<E>? = token.enumConstants
    fun random(): E {
        return values!![RND.nextInt(values.size)]
    }

    companion object {
        private val RND: Random = Random()
    }

}