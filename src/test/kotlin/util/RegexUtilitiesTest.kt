package util

import org.junit.jupiter.api.Test


internal class RegexUtilitiesTest {

    @Test
    fun test0() {
        val matcher = matchSemanticVersion("1.0.1", null)
        println(matcher.matches())
        println("Group 0:" + matcher.group(0))
        println("Group 1:" + matcher.group(1))
        println("Group 2:" + matcher.group(2))
        println("Group 3:" + matcher.group(3))
        println("Group 4:" + matcher.group(4))
        println("Group 5:" + matcher.group(5))
    }

    @Test
    fun test() {
        val matcher = matchSemanticVersion("1.0.1-rc1+asdfsfd", null)
        println(matcher.matches())
        println(matcher.group(0))
        println(matcher.group(1))
        println(matcher.group(2))
        println(matcher.group(3))
        println(matcher.group(4))
        println(matcher.group(5))
    }

    @Test
    fun test2() {
        val matcher = matchSemanticVersion("v1.0.1-rc1+asdfsfd", "v")
        println(matcher.matches())
        println(matcher.group(0))
        println(matcher.group(1))
        println(matcher.group(2))
        println(matcher.group(3))
        println(matcher.group(4))
        println(matcher.group(5))
    }

    @Test
    fun test3() {
        val matcher = matchSemanticVersion("v1.0.1-rc1", "v")
        println(matcher.matches())
        println(matcher.group(0))
        println(matcher.group(1))
        println(matcher.group(2))
        println(matcher.group(3))
        println(matcher.group(4))
        println(matcher.group(5))
    }
}

