package net.raphdf201.techapp

import com.architect.kmpessentials.fileSystem.KmpFileSystem

val file1 = KmpFileSystem.getAppDirectory().plus("/techTokens1")
val file2 = KmpFileSystem.getAppDirectory().plus("/techTokens2")
val file3 = KmpFileSystem.getAppDirectory().plus("/techTokens3")

fun store1(data: String) {
    KmpFileSystem.writeTextToFileAt(file1, data)
}

fun store2(data: String) {
    KmpFileSystem.writeTextToFileAt(file2, data)
}

fun store3(data: String) {
    KmpFileSystem.writeTextToFileAt(file3, data)
}

fun get1(): String {
    return KmpFileSystem.readTextFromFileAt(file1).toString()
}

fun get2(): String {
    return KmpFileSystem.readTextFromFileAt(file2).toString()
}

fun get3(): String {
    return KmpFileSystem.readTextFromFileAt(file3).toString()
}
