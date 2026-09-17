package com.bustedtool

import java.io.File
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import java.security.MessageDigest
import java.util.Scanner

private const val VERSION = "1.0.0"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        banner()
        help()
        return
    }

    when (args[0].lowercase()) {
        "help", "-h", "--help" -> help()
        "version", "-v", "--version" -> println("BUSTED TOOL $VERSION")
        "dns" -> dns(args.getOrNull(1))
        "headers" -> headers(args.getOrNull(1))
        "url" -> validateUrl(args.getOrNull(1))
        "ip" -> ipInfo(args.getOrNull(1))
        "port" -> portCheck(args.getOrNull(1), args.getOrNull(2))
        "hash" -> hashText(args.drop(1).joinToString(" "))
        "filehash" -> fileHash(args.getOrNull(1))
        "password" -> passwordStrength(args.drop(1).joinToString(" "))
        else -> {
            println("[!] Unknown command: ${args[0]}")
            help()
        }
    }
}

fun banner() {
    println("""
        ╔══════════════════════════════════╗
        ║          BUSTED TOOL             ║
        ║   Kotlin Cybersecurity Toolkit   ║
        ╚══════════════════════════════════╝
    """.trimIndent())
}

fun help() {
    println("""
        Commands:
          help                         Show this help
          version                      Show version
          dns <domain>                 DNS/address lookup
          ip <host>                    Resolve host information
          headers <https-url>          Inspect HTTP response headers
          url <url>                    Validate a URL
          port <host> <port>           Check one authorized TCP port
          hash <text>                  SHA-256 and SHA-512
          filehash <path>              SHA-256 of a local file
          password <text>              Basic password-strength estimate

        Examples:
          busted dns example.com
          busted headers https://example.com
          busted port 127.0.0.1 8080
          busted hash hello
    """.trimIndent())
}

fun dns(host: String?) {
    if (host.isNullOrBlank()) return println("Usage: dns <domain>")
    try {
        val addresses = InetAddress.getAllByName(host)
        println("[+] Addresses for $host:")
        addresses.forEach { println("  ${it.hostAddress}") }
    } catch (e: Exception) {
        println("[!] Lookup failed: ${e.message}")
    }
}

fun ipInfo(host: String?) {
    if (host.isNullOrBlank()) return println("Usage: ip <host>")
    try {
        val address = InetAddress.getByName(host)
        println("Host: ${address.hostName}")
        println("Address: ${address.hostAddress}")
        println("Loopback: ${address.isLoopbackAddress}")
        println("Site-local: ${address.isSiteLocalAddress}")
    } catch (e: Exception) {
        println("[!] Lookup failed: ${e.message}")
    }
}

fun headers(urlText: String?) {
    if (urlText.isNullOrBlank()) return println("Usage: headers <https-url>")
    try {
        val uri = URI(urlText)
        require(uri.scheme == "https" || uri.scheme == "http") { "Only HTTP/HTTPS URLs are supported." }
        val c = uri.toURL().openConnection() as HttpURLConnection
        c.requestMethod = "HEAD"
        c.connectTimeout = 5000
        c.readTimeout = 5000
        c.instanceFollowRedirects = true
        c.connect()
        println("HTTP ${c.responseCode} ${c.responseMessage}")
        c.headerFields.forEach { (k, v) ->
            if (k != null) println("$k: ${v.joinToString(", ")}")
        }
        c.disconnect()
    } catch (e: Exception) {
        println("[!] Request failed: ${e.message}")
    }
}

fun validateUrl(urlText: String?) {
    if (urlText.isNullOrBlank()) return println("Usage: url <url>")
    try {
        val uri = URI(urlText)
        val valid = (uri.scheme == "http" || uri.scheme == "https") && !uri.host.isNullOrBlank()
        println(if (valid) "[+] URL format looks valid." else "[!] URL format is invalid.")
        if (valid) {
            println("Scheme: ${uri.scheme}")
            println("Host: ${uri.host}")
        }
    } catch (e: Exception) {
        println("[!] Invalid URL: ${e.message}")
    }
}

fun portCheck(host: String?, portText: String?) {
    if (host.isNullOrBlank() || portText.isNullOrBlank()) return println("Usage: port <host> <port>")
    val port = portText.toIntOrNull()
        ?: return println("[!] Port must be a number.")
    if (port !in 1..65535) return println("[!] Port must be 1-65535.")
    try {
        val socket = java.net.Socket()
        socket.connect(java.net.InetSocketAddress(host, port), 1500)
        socket.close()
        println("[+] TCP $host:$port is reachable.")
    } catch (_: Exception) {
        println("[-] TCP $host:$port is not reachable.")
    }
}

fun hashText(text: String) {
    if (text.isBlank()) return println("Usage: hash <text>")
    println("SHA-256: ${digest("SHA-256", text.toByteArray())}")
    println("SHA-512: ${digest("SHA-512", text.toByteArray())}")
}

fun fileHash(pathText: String?) {
    if (pathText.isNullOrBlank()) return println("Usage: filehash <path>")
    val file = File(pathText)
    if (!file.isFile) return println("[!] File not found: $pathText")
    try {
        val md = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var n: Int
            while (input.read(buffer).also { n = it } > 0) md.update(buffer, 0, n)
        }
        println("SHA-256: ${md.digest().joinToString("") { "%02x".format(it) }}")
    } catch (e: Exception) {
        println("[!] Hashing failed: ${e.message}")
    }
}

fun passwordStrength(text: String) {
    if (text.isBlank()) return println("Usage: password <text>")
    var score = 0
    if (text.length >= 12) score++
    if (text.any(Char::isUpperCase)) score++
    if (text.any(Char::isLowerCase)) score++
    if (text.any(Char::isDigit)) score++
    if (text.any { !it.isLetterOrDigit() }) score++

    val label = when (score) {
        5 -> "Strong"
        4 -> "Good"
        3 -> "Moderate"
        else -> "Weak"
    }
    println("Strength: $label")
    println("Length: ${text.length}")
    println("Tip: use a long, unique password and never reuse it.")
}

fun digest(algorithm: String, bytes: ByteArray): String =
    MessageDigest.getInstance(algorithm).digest(bytes).joinToString("") { "%02x".format(it) }
