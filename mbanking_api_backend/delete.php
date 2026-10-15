<?php
include "config.php";

$id = $_POST['id'] ?? '';

$sql = "DELETE FROM users WHERE id='$id'";

if (mysqli_query($koneksi, $sql)) {
    echo json_encode(["success" => true, "message" => "Data berhasil dihapus"]);
} else {
    echo json_encode(["success" => false, "message" => "Gagal menghapus data"]);
}
?>