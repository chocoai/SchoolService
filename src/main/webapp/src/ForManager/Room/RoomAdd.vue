<template>
  <div class="RoomAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="班级名称" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-sub-header>班级课程：</mu-sub-header>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName1" @click="openCourse1=true" :icon="courseIcon1" :backgroundColor="courseBack1" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName2" @click="openCourse2=true" :icon="courseIcon2" :backgroundColor="courseBack2" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName3" @click="openCourse3=true" :icon="courseIcon3" :backgroundColor="courseBack3" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName4" @click="openCourse4=true" :icon="courseIcon4" :backgroundColor="courseBack4" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName5" @click="openCourse5=true" :icon="courseIcon5" :backgroundColor="courseBack5" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName6" @click="openCourse6=true" :icon="courseIcon6" :backgroundColor="courseBack6" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName7" @click="openCourse7=true" :icon="courseIcon7" :backgroundColor="courseBack7" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName8" @click="openCourse8=true" :icon="courseIcon8" :backgroundColor="courseBack8" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName9" @click="openCourse9=true" :icon="courseIcon9" :backgroundColor="courseBack9" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName10" @click="openCourse10=true" :icon="courseIcon10" :backgroundColor="courseBack10" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName11" @click="openCourse11=true" :icon="courseIcon11" :backgroundColor="courseBack11" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName12" @click="openCourse12=true" :icon="courseIcon12" :backgroundColor="courseBack12" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-drawer right :open="openCourse1" docked="false">
      <mu-appbar title="请选择班主任" @click.native="openCourse1=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course1=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course1" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse2" docked="false">
      <mu-appbar title="请选择语文老师" @click.native="openCourse2=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course2=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course2" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse3" docked="false">
      <mu-appbar title="请选择数学老师" @click.native="openCourse3=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course3=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course3" label="" labelLeft  :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse4" docked="false">
      <mu-appbar title="请选择英语老师" @click.native="openCourse4=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course4=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course4" label="" labelLeft  :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse5" docked="false">
      <mu-appbar title="请选择音乐老师" @click.native="openCourse5=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course5=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course5" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse6" docked="false">
      <mu-appbar title="请选择体育老师" @click.native="openCourse6=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course6=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course6" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse7" docked="false">
      <mu-appbar title="请选择美术老师" @click.native="openCourse7=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course7=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course7" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse8" docked="false">
      <mu-appbar title="请选择科学老师" @click.native="openCourse8=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course8=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course8" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse9" docked="false">
      <mu-appbar title="请选择计算机老师" @click.native="openCourse9=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course9=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course9" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse10" docked="false">
      <mu-appbar title="请选择品生老师" @click.native="openCourse10=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course10=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course10" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse11" docked="false">
      <mu-appbar title="请选择品社老师" @click.native="openCourse11=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course11=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course11" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse12" docked="false">
      <mu-appbar title="请选择传统文化老师" @click.native="openCourse12=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="course12=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="course12" label="" labelLeft :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" :disabled="saveAble" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
  </div>
</template>

<script>
import * as API from './RoomAPI.js'
export default {
  name: 'RoomAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      icon: '',
      color: '',
      name: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      courseIcon1: 'bookmark_border',
      courseIcon2: 'bookmark_border',
      courseIcon3: 'bookmark_border',
      courseIcon4: 'bookmark_border',
      courseIcon5: 'bookmark_border',
      courseIcon6: 'bookmark_border',
      courseIcon7: 'bookmark_border',
      courseIcon8: 'bookmark_border',
      courseIcon9: 'bookmark_border',
      courseIcon10: 'bookmark_border',
      courseIcon11: 'bookmark_border',
      courseIcon12: 'bookmark_border',
      courseBack1: '#66CCCC',
      courseBack2: '#66CCCC',
      courseBack3: '#66CCCC',
      courseBack4: '#66CCCC',
      courseBack5: '#66CCCC',
      courseBack6: '#66CCCC',
      courseBack7: '#66CCCC',
      courseBack8: '#66CCCC',
      courseBack9: '#66CCCC',
      courseBack10: '#66CCCC',
      courseBack11: '#66CCCC',
      courseBack12: '#66CCCC',
      courseName1: '班主任',
      courseName2: '语文',
      courseName3: '数学',
      courseName4: '英语',
      courseName5: '音乐',
      courseName6: '体育',
      courseName7: '美术',
      courseName8: '科学',
      courseName9: '计算机',
      courseName10: '品生',
      courseName11: '品社',
      courseName12: '传统文化',
      openCourse1: false,
      openCourse2: false,
      openCourse3: false,
      openCourse4: false,
      openCourse5: false,
      openCourse6: false,
      openCourse7: false,
      openCourse8: false,
      openCourse9: false,
      openCourse10: false,
      openCourse11: false,
      openCourse12: false,
      course1: [],
      course2: [],
      course3: [],
      course4: [],
      course5: [],
      course6: [],
      course7: [],
      course8: [],
      course9: [],
      course10: [],
      course11: [],
      course12: [],
      teachers: [],
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.TeacherList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.teachers = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'report_problem', 'orange')
    })
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    },
    courseBack1: function () {
      if (this.course1.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack2: function () {
      if (this.course2.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack3: function () {
      if (this.course3.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack4: function () {
      if (this.course4.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack5: function () {
      if (this.course5.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack6: function () {
      if (this.course6.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack7: function () {
      if (this.course7.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack8: function () {
      if (this.course8.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack9: function () {
      if (this.course9.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack10: function () {
      if (this.course10.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack11: function () {
      if (this.course11.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack12: function () {
      if (this.course12.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseIcon1: function () {
      if (this.course1.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon2: function () {
      if (this.course2.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon3: function () {
      if (this.course3.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon4: function () {
      if (this.course4.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon5: function () {
      if (this.course5.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon6: function () {
      if (this.course6.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon7: function () {
      if (this.course7.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon8: function () {
      if (this.course8.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon9: function () {
      if (this.course9.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon10: function () {
      if (this.course10.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon11: function () {
      if (this.course11.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    courseIcon12: function () {
      if (this.course12.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/roomList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.name = ''
      this.nameErrorColor = ''
      this.course1 = []
      this.course2 = []
      this.course3 = []
      this.course4 = []
      this.course5 = []
      this.course6 = []
      this.course7 = []
      this.course8 = []
      this.course9 = []
      this.course10 = []
      this.course11 = []
      this.course12 = []
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '班级名称为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/\d{4}[\u7ea7]\d{1,2}[\u73ed]/.test(value)) {
        this.nameErrorText = '班级名称格式应为：XXXX级XX班'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckNameForNew,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = 'OK'
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          course1: this.course1,
          course2: this.course2,
          course3: this.course3,
          course4: this.course4,
          course5: this.course5,
          course6: this.course6,
          course7: this.course7,
          course8: this.course8,
          course9: this.course9,
          course10: this.course10,
          course11: this.course11,
          course12: this.course12
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/roomList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    }
  }
}
</script>
<style lang="css">
  .popup-bottom {
    width: 100%;
    opacity: .8;
    height: 48px;
    line-height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    max-width: 300px;
  }
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
