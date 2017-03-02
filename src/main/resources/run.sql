### 操作数据表teacher
#namespace("teacher")
  #sql("login_teacher")
    select * from teacher
    where (login = #p(login) and pass = #p(pass) and state = #p(state))
      or (mobile = #p(mobile) and pass = #p(pass) and state = #p(state))
      or (email = #p(email) and pass = #p(pass) and state = #p(state))
  #end
  #sql("login_manager")
    select * from teacher
    where (login = #p(login) and pass = #p(pass) and state = #p(state) and isManager = 1)
      or (mobile = #p(mobile) and pass = #p(pass) and state = #p(state) and isManager = 1)
      or (email = #p(email) and pass = #p(pass) and state = #p(state) and isManager = 1)
  #end
  #sql("weixin_teacher")
    select * from teacher where userId = ? and state = ?
  #end
  #sql("weixin_manager")
    select * from teacher where userId = ? and state = ? and isManager = 1
  #end
  #sql("teacher_mobile")
    select * from teacher where mobile = ?
  #end
  #sql("teacher_userId")
    select * from teacher where userId = ?
  #end
  #sql("teacher_email")
    select * from teacher where email = ?
  #end
#end

### 操作数据表parent
#namespace("parent")
  #sql("login_parent")
    select * from parent
    where (login = #p(login) and pass = #p(pass) and state = #p(state))
      or (mobile = #p(mobile) and pass = #p(pass) and state = #p(state))
      or (email = #p(email) and pass = #p(pass) and state = #p(state))
  #end
  #sql("parent_mobile")
    select * from parent where mobile = ?
  #end
  #sql("parent_userId")
    select * from parent where userId = ?
  #end
  #sql("parent_email")
    select * from parent where email = ?
  #end
#end