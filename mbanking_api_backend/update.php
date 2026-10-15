<?php
include "config.php";

$id       = $_POST['id'] ?? '';
$password = $_POST['password'] ?? '';
$pin      = $_POST['pin'] ?? '';

if (!empty($password) && !empty($pin)) {
    $sql = "UPDATE users SET password='$password', pin='$pin' WHERE id='$id'";
} elseif (!empty($password)) {
    $sql = "UPDATE users SET password='$password' WHERE id='$id'";
} elseif (!empty($pin)) {
    $sql = "UPDATE users SET pin='$pin' WHERE id='$id'";
} else {
    echo json_encode(["success" => false, "message" => "Tidak ada data yang diupdate"]);
    exit();
}

if (mysqli_query($koneksi, $sql)) {
    echo json_encode(["success" => true, "message" => "Data berhasil diupdate"]);
} else {
    echo json_encode(["success" => false, "message" => "Gagal mengupdate data"]);
}
?>