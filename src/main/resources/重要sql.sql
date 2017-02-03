select student.`name` as sname,identity.`name` as dname,enterprise.`name` as pname,roomworkread.time,count(distinct student.number)
from ((((((student
left join room on student.room_id = room.id)
left join relation on student.id = relation.student_id)
left join identity on identity.id = relation.parent_id)
left join enterprise on enterprise.id = relation.parent_id)
left join roomworkread on roomworkread.parent_id=relation.parent_id)
left join roomwork on roomworkread.roomwork_id = roomwork.id)
where roomwork.id=1 and roomworkread.state=0