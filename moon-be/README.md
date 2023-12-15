Moon Backend Docs

# Cài đặt Database
1. Download PostgreSQL: https://www.postgresql.org/download/
2. Tiến hành cài đặt bình thường. Lưu ý: ghi nhớ mật khẩu đặt cho user postgres

	Cần nhớ: (1)
    - username: postgres
      - password: "password" (thay "password" bằng mật khẩu của bạn)
3. Khởi động "pgAdmin 4" - công cụ để tương tác với CSDL PostgreSQL
4. Mở "Servers", mở "PostgreSQL", chuột phải chọn "Create", chọn "Databse"
5. Trong ô Database, đặt tên database như sau: moon_android_app
6. Chọn "Save", tạo database thành công

# Clone project

# Khởi động project
1. Mở file application.yml trong đường dẫn sau: moon-be/src/main/resources/application.yml
2. Chỉnh sửa thông tin username, password giống (1)
3. Chạy project

# Giới thiệu

Hiện tại, Backend cung cấp những API sau:

### Những API được truy cập trực tiếp:
1. Post - Register: http://localhost:8080/api/v1/auth/register

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

2. Post - Authenticate: http://localhost:8080/api/v1/auth/authenticate

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

1. Post - Logout: http://localhost:8080/api/v1/auth/logout

- Đăng xuất tài khoản người dùng.

2. Get - Get all songs: http://localhost:8080/api/v1/songs/all

- Liệt kê danh sách bài hát trong server.

- Server phản hồi về một file JSON chứa danh sách thông tin các bài hát được lưu trên server.

         [
             {
                 "id": 1,
                 "title": "title",
                 "artist": "artist",
                 "genre": "genre",
                 "filePath": "filePath"
             }
         ]

3. Get - Get detail song: http://localhost:8080/api/v1/songs/ + "id"

- Ví dụ: http://localhost:8080/api/v1/songs/1

- Tìm kiếm thông tin một bài hát bằng id.

- Server phản hồi về một file JSON chứa thông tin bài hát cần tìm:

        {
            "id": 1,
            "title": "title",
            "artist": "artist",
            "genre": "genre",
            "filePath": "filePath"
        }

4. Get - Search title: http://localhost:8080/api/v1/songs/search/title?title= + "title"

- Ví dụ: http://localhost:8080/api/v1/songs/search/title?title=abc

- Tìm kiếm các bài hát theo title.

- Server phản hồi về một file JSON chứa danh sách thông tin các bài hát tìm theo tiêu đề:

		[
		{
            "id": 1,
            "title": "abc",
            "artist": "artist",
            "genre": "genre",
            "filePath": "filePath"
        },
        {
            "id": 2,
            "title": "abc123",
            "artist": "artist",
            "genre": "genre",
            "filePath": "filePath"
        }
		]

5. Get - Search artist: http://localhost:8080/api/v1/songs/search/artist?artist= + "artist"

- Ví dụ: http://localhost:8080/api/v1/songs/search/artist?artist=abc

- Tìm kiếm các bài hát theo artist.

6. Get - Search genre: http://localhost:8080/api/v1/songs/search/genre?genre= + "genre"

- Ví dụ: http://localhost:8080/api/v1/songs/search/genre?genre=abc

- Tìm kiếm các bài hát theo genre.

7. Post - Add song: http://localhost:8080/api/v1/songs/add

- Thêm một bài hát lên server.

- Yêu cầu một file JSON body có dạng:
  
      {
          "title": "title",
          "artist": "artist",
          "genre": "genre",
          "filePath": "filePath"
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

8. Put - Update song: http://localhost:8080/api/v1/songs/ + "id" + /update

- Ví dụ: http://localhost:8080/api/v1/songs/1/update

- Chỉnh sửa thông tin một bài hát.

- Yêu cầu một file JSON body có dạng:

        {
            "title": "title",
            "artist": "artist",
            "genre": "genre",
            "filePath": "filePath"
        }
- Khi chỉnh sửa thành công, server gửi về một file JSON:

        {
          "statusCode": 200,
          "message": "Song updated success"
        }

9. Delete - Delete song: http://localhost:8080/api/v1/songs/ + "id" + /delete

- Ví dụ: http://localhost:8080/api/v1/songs/1/delete

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