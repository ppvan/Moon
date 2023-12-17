Moon Backend Docs

# Cài đặt Database (Local)
1. Download PostgreSQL: https://www.postgresql.org/download/
2. Tiến hành cài đặt bình thường. Lưu ý: ghi nhớ mật khẩu đặt cho user postgres. Cần nhớ: (1)
    - username: postgres
    - password: "password" (thay "password" bằng mật khẩu của bạn)
3. Khởi động "pgAdmin 4" - công cụ để tương tác với CSDL PostgreSQL
4. Mở "Servers", mở "PostgreSQL", chuột phải chọn "Create", chọn "Databse"
5. Trong ô Database, đặt tên database như sau: moon_android_app
6. Chọn "Save", tạo database thành công

# Clone project

# Khởi động project (Local)
1. Mở file application.yml trong đường dẫn sau: moon-be/src/main/resources/application.yml
2. Chỉnh sửa thông tin username, password giống (1)
3. Chạy project

# Giới thiệu

Hiện tại, Backend cung cấp những API sau:

### Những API được truy cập trực tiếp:

1. Post - Register: http://139.59.227.169:8080/api/v1/auth/register
    - Đăng ký tài khoản, yêu cầu một file JSON có dạng như sau:

          {
            "firstname": "test",
            "lastname": "test",
            "email": "test@gmail.com",
            "password": "test"
          }
     
    - Có 2 trường hợp xảy ra khi gửi request đến server:
        1. Đăng ký thành công:
            - Server phản hồi về một file JSON:

                  {
                    "statusCode": 200,
                    "message": "User registered success"
                  }

        2. Đăng ký thất bại, email đã tồn tại:
            - Server phản hồi về một file JSON:

                  {
                    "statusCode": 409,
                    "message": "Email already exists"
                  }

2. Post - Authenticate: http://139.59.227.169:8080/api/v1/auth/authenticate
    - Đăng nhập tài khoản, yêu cầu một file JSON có dạng như sau:

          {
            "email": "test@gmail.com",
            "password": "test"
          }

    - Có 2 trường hợp xảy ra khi gửi request đến server:
        1. Đăng nhập thành công:
            - Server phản hồi về một file JSON:
     
                  {
                    "access_token": "(mã token)",
                    "refresh_token": "(mã token)"
                  }
       
        2. Đăng nhập thất bại:
            - Server trả về mã 403, đăng nhập thất bại.

### Những API được bảo vệ:

- Tất cả những API được bảo vệ đều cần access_token để có thể truy cập.

1. Post - Logout: http://139.59.227.169:8080/api/v1/auth/logout
    - Đăng xuất tài khoản người dùng.
2. Get - Get all songs: http://139.59.227.169:8080/api/v1/songs/all
    - Liệt kê danh sách bài hát trong server.
    - Server phản hồi về một file JSON chứa danh sách thông tin các bài hát được lưu trên server.
 
          [
            {
              "id": 1,
              "title": "title1",
              "artist": "artist1",
              "album": "album1",
              "thumbnail": "thumbnail1", 
              "filePath": "filePath1"
            }, 
            {
              "id": 2,
              "title": "title2",
              "artist": "artist2",
              "album": "album2",
              "thumbnail": "thumbnail2", 
              "filePath": "filePath2"
            }
          ]

3. Get - Get detail song: http://139.59.227.169:8080/api/v1/songs/ + "id"
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/1
    - Tìm kiếm thông tin một bài hát bằng id.
    - Server phản hồi về một file JSON chứa thông tin bài hát cần tìm:

          {
            "id": 1,
            "title": "title",
            "artist": "artist",
            "album": "album",
            "thumbnail": "thumbnail",
            "filePath": "filePath"
          }

4. Get - Search title: http://139.59.227.169:8080/api/v1/songs/search/title?title= + "title"
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/search/title?title=abc
    - Tìm kiếm các bài hát theo title.
    - Server phản hồi về một file JSON chứa danh sách thông tin các bài hát tìm theo tiêu đề:

          [
            {
              "id": 1,
              "title": "abc",
              "artist": "artist1",
              "album": "album1",
              "thumbnail": "thumbnail1",
              "filePath": "filePath1"
            },
            {
              "id": 2,
              "title": "abc123",
              "artist": "artist2",
              "album": "album2",
              "thumbnail": "thumbnail2",
              "filePath": "filePath2"
            }
          ]

5. Get - Search artist: http://139.59.227.169:8080/api/v1/songs/search/artist?artist= + "artist"
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/search/artist?artist=abc
    - Tìm kiếm các bài hát theo artist.
6. Get - Search album: http://139.59.227.169:8080/api/v1/songs/search/album?album= + "album"
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/search/album?album=abc
    - Tìm kiếm các bài hát theo album.
7. Post - Upload song: http://139.59.227.169:8080/api/v1/songs/upload
    - Thêm một bài hát lên server.
    - Yêu cầu một file JSON body có dạng:
    - Thumbnail và filePath định dạng file.
  
          {
            "title": "title1",
            "artist": "artist1",
            "album": "album1",
            "thumbnail": "thumbnail1",
            "filePath": "filePath1"
          }

    - Có 2 trường hợp xảy ra:
        1. Thêm bài hát mới thành công, server phản hồi về một file JSON:

                {
                  "statusCode": 201,
                  "message": "Song added success"
                }

        2. Thêm bài hát mới thất bại, không điền đầy đủ thông tin:

                {
                  "statusCode": 404,
                  "message": "No song details found"
                }   

8. Put - Update song: http://139.59.227.169:8080/api/v1/songs/ + "id" + /update
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/1/update
    - Chỉnh sửa thông tin một bài hát.
    - Yêu cầu một file JSON body có dạng:

          {
            "title": "title",
            "artist": "artist",``
            "album": "album"
          }
    - Khi chỉnh sửa thành công, server gửi về một file JSON:

          {
            "statusCode": 200,
            "message": "Song updated success"
          }

    - Khi chỉnh sửa thất bại:

          {
            "statusCode": 404,
            "message": "Song could not be updated"
          }

9. Delete - Delete song: http://139.59.227.169:8080/api/v1/songs/ + "id" + /delete
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/1/delete
    - Xóa một bài hát theo id.
    - Khi xóa thành công:

          {
            "statusCode": 204,
            "message": "Song deleted success"
          }

    - Khí xóa thất bại:

        {
          "statusCode": 404,
          "message": "Song could not be deleted"
        }
    
10. Get - Suggestion song: http://139.59.227.169:8080/api/v1/songs/suggestions?keyword= + "word"
    - Ví dụ: http://139.59.227.169:8080/api/v1/songs/suggestions?keyword=abc
    - API cải tiến từ 3 API search title / artist / album
    - Trả về một danh sách các cụm từ liên quan với keyword.
    - Phục vụ cho chức năng thanh tìm kiếm, đề xuất.
11. Get - Download file: http://139.59.227.169:8080/api/v1/songs/file/ + "file.mp3"
    - Yêu cầu một tên file bài hát, lấy từ filepath.
    - Trả về một tệp nhị phân là nội dung file.