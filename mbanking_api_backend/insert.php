<?php
include "config.php";

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';
$pin      = $_POST['pin'] ?? '';

$sql = "INSERT INTO users (username, password, pin) VALUES ('$username', '$password', '$pin')";

if (mysqli_query($koneksi, $sql)) {
    echo json_encode(["success" => true, "message" => "Data berhasil disimpan"]);
} else {
    echo json_encode(["success" => false, "message" => "Gagal menyimpan data"]);
}
?>