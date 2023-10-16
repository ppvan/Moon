# Moon

## Phía front-end

Giao diện tab, là một app nghe nhạc offline bth
có chức năng upload và sync nhạc lên server

- Danh sách nhạc
- View khi phát nhạc
- tua, next các kiểu

## Phía backend

Gồm hai thực thể chính là 
- User
- Song
- Playlist

Yêu thích cũng chỉ là playlist đặc biệt

quan hệ 1 User -> n playlist
1 playlist -> n song

Phía server cần có CRUD các thực thể này

- Quản lý user (có sẵn trong Spring, game dễ)
- Quản lý playlist (CRUD)
- Quản lý bài hát (Chỉ sync thôi, muốn nghe thì phải sync từ mobile trước)

## Hành vi

Sau khi cài đặt -> bắt người dùng đăng ký tài khoản
Quét các file nhạc trên máy




