### 操作数据表teacher
#namespace("teacher")
  #sql("login_teacher")
    SELECT * FROM teacher
    WHERE (login = #p(login) AND pass = #p(pass) AND state = #p(state))
      OR (mobile = #p(mobile) AND pass = #p(pass) AND state = #p(state))
      OR (email = #p(email) AND pass = #p(pass) AND state = #p(state))
  #end
  #sql("login_manager")
    SELECT * FROM teacher
    WHERE (login = #p(login) AND pass = #p(pass) AND state = #p(state) AND isManager = 1)
      OR (mobile = #p(mobile) AND pass = #p(pass) AND state = #p(state) AND isManager = 1)
      OR (email = #p(email) AND pass = #p(pass) AND state = #p(state) AND isManager = 1)
  #end
  #sql("weixin_teacher")
    select * FROM teacher WHERE userId = ? AND state = ?
  #end

  #sql("weixin_manager")
    SELECT * FROM teacher WHERE userId = ? AND state = ? AND isManager = 1
  #end
  #sql("mobile")
    SELECT * FROM teacher WHERE mobile = ?
  #end
  #sql("userId")
    SELECT * FROM teacher WHERE userId = ?
  #end
  #sql("email")
    SELECT * FROM teacher WHERE email = ?
  #end
  #sql("list")
    SELECT * FROM teacher WHERE state = 1 OR state = 2 ORDER BY id ASC
  #end
#end

### 操作数据表parent
#namespace("parent")
  #sql("login_parent")
    SELECT * FROM parent
    WHERE (login = #p(login) AND pass = #p(pass) AND state = #p(state))
      OR (mobile = #p(mobile) AND pass = #p(pass) AND state = #p(state))
      OR (email = #p(email) AND pass = #p(pass) AND state = #p(state))
  #end
  #sql("weixin_parent")
    SELECT * FROM parent WHERE userId = ? AND state = ?
  #end
  #sql("mobile")
    SELECT * FROM parent WHERE mobile = ?
  #end
  #sql("userId")
    SELECT * FROM parent WHERE userId = ?
  #end
  #sql("email")
    SELECT * FROM parent WHERE email = ?
  #end
#end

### 操作数据表relation
#namespace("relation")
  #sql("parentId")
    SELECT * FROM relation WHERE parent_id = ?
  #end
  #sql("getParent")
    SELECT * FROM relation WHERE student_id = ? AND identity_id = ?
  #end
#end

### 操作数据表student
#namespace("student")
  #sql("id")
    SELECT * FROM student WHERE id = ?
  #end
#end

### 操作数据表identity
#namespace("identity")
  #sql("id")
    SELECT * FROM identity WHERE id = ?
  #end
#end