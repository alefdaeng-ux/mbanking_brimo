<?php
include "config.php";

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

$sql = "SELECT id, username FROM users WHERE username='$username' AND password='$password'";
$result = mysqli_query($koneksi, $sql);

if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    echo json_encode([
        "success" => true,
        "message" => "Login berhasil",
        "data" => $row
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Username atau password salah"
    ]);
}
?>