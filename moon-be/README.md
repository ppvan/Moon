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

1. Post - Register: http://139.59.227.169:8080/api/auth/register
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

2. Post - Authenticate: http://139.59.227.169:8080/api/auth/authenticate
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

<!-- API Authentication -->
1. Post - Logout: http://139.59.227.169:8080/api/auth/logout
    - Đăng xuất tài khoản người dùng.
    - Yêu cầu token của người dùng.

<!-- API Song -->
2. Get - Get all songs: http://139.59.227.169:8080/api/songs/
    - Liệt kê danh sách bài hát trong server.
    - Yêu cầu token của người dùng.
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

3. Get - Get detail song: http://139.59.227.169:8080/api/songs/ + "id"
    - Ví dụ: http://139.59.227.169:8080/api/songs/1
    - Tìm kiếm thông tin một bài hát bằng id.
    - Yêu cầu token của người dùng.
    - Server phản hồi về một file JSON chứa thông tin bài hát cần tìm:

          {
            "id": 1,
            "title": "title",
            "artist": "artist",
            "album": "album",
            "thumbnail": "thumbnail",
            "filePath": "filePath"
          }

4. Get - Search title: http://139.59.227.169:8080/api/songs/title/ + "title"
    - Ví dụ: http://139.59.227.169:8080/api/songs/title/abc
    - Tìm kiếm các bài hát theo title.
    - Yêu cầu token của người dùng.
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

5. Get - Search artist: http://139.59.227.169:8080/api/songs/artist/ + "artist"
    - Ví dụ: http://139.59.227.169:8080/api/songs/artist/abc
    - Tìm kiếm các bài hát theo artist.
    - Yêu cầu token của người dùng.

6. Get - Search album: http://139.59.227.169:8080/api/songs/album/ + "album"
    - Ví dụ: http://139.59.227.169:8080/api/songs/album/abc
    - Tìm kiếm các bài hát theo album.
    - Yêu cầu token của người dùng.

7. Post - Upload song: http://139.59.227.169:8080/api/songs/
    - Thêm một bài hát lên server.
    - Yêu cầu token của người dùng.
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

8. Patch - Update song: http://139.59.227.169:8080/api/songs/ + "id"
    - Ví dụ: http://139.59.227.169:8080/api/songs/1
    - Chỉnh sửa thông tin một bài hát.
    - Yêu cầu token của người dùng.
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

9. Delete - Delete song: http://139.59.227.169:8080/api/songs/ + "id"
    - Ví dụ: http://139.59.227.169:8080/api/songs/1
    - Xóa một bài hát theo id.
    - Yêu cầu token của người dùng.
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
    
10. Get - Suggestion song: http://139.59.227.169:8080/api/songs/suggestions/+ "word"
    - Ví dụ: http://139.59.227.169:8080/api/songs/suggestions/abc
    - API cải tiến từ 3 API search title / artist / album
    - Trả về một danh sách các cụm từ liên quan với keyword.
    - Phục vụ cho chức năng thanh tìm kiếm, đề xuất.
    - Yêu cầu token của người dùng.

11. Get - Download file: http://139.59.227.169:8080/api/songs/ + "file.mp3" + /file
    - Yêu cầu một tên file bài hát, lấy từ filepath.
    - Trả về một tệp nhị phân là nội dung file.
    - Yêu cầu token của người dùng.

12. Get - Download image: http://139.59.227.169:8080/api/songs/ + "image.png" + /image
    - Yêu cầu token của người dùng.
    - Yêu cầu một tên file ảnh.
    - Trả về ảnh.
    - Đây là thumbnail của bài hát.

<!-- API Profile -->
13. Get - Get Profile: http://139.59.227.169:8080/api/profile/
    - Yêu cầu token của người dùng.
    - Trả về một file json thông tin người dùng.

14. Patch - Update Profile: http://139.59.227.169:8080/api/profile/
    - Yêu cầu token của người dùng.
    - Yêu cầu một file json trong body request.
          
          {
            "firstname": "admin1",
            "lastname": "admin1",
            "avatar": "avatar"
          }
    
15. Patch - Upload Avatar: http://139.59.227.169:8080/api/profile/avatar
    - Yêu cầu token của người dùng.
    - Yêu cầu một file

16. Get - Download Avatar: http://139.59.227.169:8080/api/profile/avatar/ + "nameFile"
    - Yêu cầu token người dùng

<!-- API Playlist -->
17. Get - Get All Playlist of user: http://139.59.227.169:8080/api/playlist/
    - Yêu cầu token người dùng
    - Trả về một file Json là danh sách các Playlist

          [
            {
                "id": 1,
                "name": "test1",
                "userName": "user1"
            },
            {
                "id": 2,
                "name": "test2",
                "userName": "user1"
            }
          ]

18. Post - Create a Playlist: http://139.59.227.169:8080/api/playlist/
    - Yêu cầu token người dùng
    - Yêu cầu một file Json

          {
            "name": "test"
          }
    
    - Tạo thành công một Playlist sẽ có thông báo:

          {
            "statusCode": 201,
            "message": "Playlist created success"
          }

19. Patch - Update Playlist: http://139.59.227.169:8080/api/playlist/ + "id"
    - Yêu cầu token người dùng
    - id là PathVariable
    - Yêu cầu một file Json

          {
            "name": "newtest"
          } 

    - Thành công sẽ có thông báo:

          {
            "statusCode": 200,
            "message": "Playlist updated success"
          }

    - Thất bại sẽ có thông báo:

          {
            "statusCode": 404,
            "message": "Playlist could not be updated"
          }

20. Delete - Delete Playlist: http://139.59.227.169:8080/api/playlist/ + "id"
    - Yêu cầu token người dùng
    - id là PathVariable
    - Xóa thành công:

          {
            "statusCode": 204,
            "message": "Playlist deleted successfully"
          }

    - Xóa thất bại:

          {
            "statusCode": 500,
            "message": "Error deleting playlist"
          }

<!-- API Playlist Item -->
21. Get - Get all playlist item(song): http://139.59.227.169:8080/api/playlist/ + "id"
    - Yêu cầu token người dùng
    - Cần id của playlist
    - id là PathVariable

22. Post - Add song to playlist: http://139.59.227.169:8080/api/playlist/ + "playlistId" + /song/ + "songId"
    - Yêu cầu token người dùng
    - Cần id của playlist
    - Cần id của song
    - id là PathVariable
    - Thêm thành công:

          {
            "statusCode": 200,
            "message": "Add song to playlist successfully"
          }

    - Thêm thất bại:

          {
            "statusCode": 500,
            "message": "Song could not add to playlist"
          }

23. Delete - Remove song from playlist: http://139.59.227.169:8080/api/playlist/ + "playlistId" + /song/ + "songId"
    - Yêu cầu token người dùng
    - Cần id của playlist
    - Cần id của song
    - id là PathVariable
    - Xóa thành công:

        {
          "statusCode": 204,
          "message": "Item deleted successfully"
        }

    - Xóa thất bại: 

        {
          "statusCode": 500,
          "message": "Error deleting playlist"
        }