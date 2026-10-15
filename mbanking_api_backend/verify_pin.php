<?php
include "config.php";

$username = $_POST['username'] ?? '';
$pin      = $_POST['pin'] ?? '';

$sql = "SELECT id FROM users WHERE username='$username' AND pin='$pin'";
$result = mysqli_query($koneksi, $sql);

if (mysqli_num_rows($result) > 0) {
    echo json_encode([
        "success" => true,
        "message" => "PIN benar"
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "PIN salah"
    ]);
}
?>