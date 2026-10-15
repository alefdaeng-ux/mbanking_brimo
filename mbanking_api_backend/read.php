<?php
include "config.php";

$data = mysqli_query($koneksi, "SELECT id, username, pin FROM users");
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    $result[] = $row;
}

echo json_encode($result);
?>