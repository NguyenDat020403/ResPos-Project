					PHẦN MỀM QUẢN LÝ NHÀ HÀNG - ResM

**1. Tổng quan**
Dự án  là một Hệ Thống Quản Lý Nhà Hàng hoạt động trên ba nền tảng: Web, Windows Form, và ứng dụng Android.
Mục tiêu của hệ thống là tự động hóa và tối ưu hóa các quy trình quản lý nhà hàng như đặt bàn, quản lý thực đơn,
 xử lý đơn hàng, kiểm soát kho, và tương tác thời gian thực với khách hàng tại bàn.
Trong đó:
 - Website: Quản lý đặt bàn, cập nhật thực đơn và theo dõi đơn hàng. (User/Admin)
 - Windows Form: Dành cho nhân viên nhà hàng quản lý đơn hàng, hóa đơn và theo dõi bàn trống.
 - Android: Dành cho khách hàng gọi món trực tiếp tại bàn.
Các thành phần hệ thống:
- Frontend: JavaScript, Windows Forms cho máy trạm và Android app.
- Backend: Sử dụng ASP.NET Core API để quản lý logic và giao tiếp giữa các thành phần.
- Cơ sở dữ liệu: SQL Server.
  
**2. Chức năng chính**
- Đặt Bàn Trực Tuyến: Khách hàng có thể đặt bàn thông qua ứng dụng web.
- Quản Lý Thực Đơn: Hiển thị thực đơn của nhà hàng kèm hình ảnh và thông tin chi tiết từng món ăn. Nhân viên có thể quản lý thực đơn.
- Quản Lý Đơn Hàng: Khách hàng gọi món qua ứng dụng Android, nhân viên sẽ quản lý đơn hàng qua ứng dụng Windows Form.
- Kiểm Soát Kho: Theo dõi mức tồn kho và quản lý các đơn hàng từ nhà cung cấp.
- Quản Lý Thanh Toán: Xử lý thanh toán và tạo hóa đơn cho khách hàng.
- Thông Báo: Thông báo tức thì cho nhân viên khi khách hàng gọi món hoặc yêu cầu trợ giúp.
  
**3. Cấu trúc dự án**
Ứng Dụng Web
- Khách Hàng: Đăng ký, đăng nhập và đặt bàn trực tuyến.
- Nhân Viên Nhà Hàng: Quản lý đặt bàn, đơn hàng và thực đơn.
- Bảng Điều Khiển Quản Lý: Quản lý người dùng, theo dõi doanh thu và giám sát hoạt động nhà hàng.
Ứng Dụng Windows Form
- Quản Lý Đơn Hàng: Nhân viên nhà hàng xử lý đơn hàng và theo dõi tiến trình.
- Quản Lý Bàn: Giám sát tình trạng bàn trống và đơn hàng của khách hàng.
- Xử Lý Thanh Toán: Tạo hóa đơn và hoàn tất thanh toán cho khách hàng.
Ứng Dụng Android
- Hiển Thị Thực Đơn: Khách hàng có thể xem thực đơn và chọn món để gọi.
- Xử Lý Đơn Hàng: Khách hàng gọi món trực tiếp từ ứng dụng.
- Thông Báo: Gửi thông báo tới nhân viên khi khách đặt món hoặc yêu cầu trợ giúp.

**4. Tiến Độ Phát Triển**

**Giai đoạn 1: Phân Tích Yêu Cầu và Thiết Kế Hệ Thống (Đã hoàn thành - Tháng 9)**
- Cấu trúc cơ sở dữ liệu đã được thiết kế với các bảng quan trọng như Customers, Tables, Orders, MenuItems, v.v.

**Giai đoạn 2: Phát Triển Ứng Dụng Web và Windows Form và Android App (Đã hoàn thành -Tháng 9)**
- Ứng dụng web: Đã cơ bản có chức năng cần thiết (Quản lý món ăn, tài khoản, ..)
- Ứng dụng Windows Form: Dành cho nhân viên nhà hàng quản lý đơn hàng và bàn.
- Ứng dụng Android: Đã hoàn thành chức năng gọi món tại bàn và gửi đơn hàng tới hệ thống.

**Giai đoạn 3: Phát Triển Ứng Dụng Web và Windows Form và Android App (Chưa hoàn thành -Tháng 10)**
- Ứng dụng web phát triển chức năng đặt bàn.
- Ứng dụng Windows Form phát triển chức năng xuất hóa đớn, thanh toán tại quầy.
- Ứng dụng Android phát tiển chứ năng gọi nhân viên từ ứng dụng.

**Giai đoạn 4: Tích hợp các hệ thống (tháng 10)**
- Đồng bộ dữ liệu giữa web, Android app và Windows Forms.

**Giai đoạn 5: Kiểm thử và triển khai (tháng 11)**

**5. Phụ lục**
Mã nguồn:
- Website: JavaScript ()
- Windows Form: C# ()
- Android App: Phát triển bằng Java trên các thiết bị tablet (_https://github.com/NguyenDat020403/ResPos-Project_)

