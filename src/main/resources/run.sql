#namespace("teacher")
  #sql("login")
    select * from teacher where login = #p(login) and pass = #p(pass) and state = #p(state)
  #end
  #sql("findTeacherByLoginPassState")
    select * from teacher
    where (login = #p(login) and pass = #p(pass) and state = #p(state))
          or (mobile = #p(mobile) and pass = #p(pass) and state = #p(state))
          or (email = #p(email) and pass = #p(pass) and state = #p(state))
  #end
#end