<template>
  <div class="layout">
    <Menu mode="horizontal" theme="dark" active-name="1">
      <div class="layout-nav">
        <Menu-item name="3">
          <Icon type="ios-analytics"></Icon>
          退出
        </Menu-item>
      </div>
    </Menu>
    <div class="layout-content">
      <br>
      <Form :label-width="100">
        <Form-item label="申请类别" required>
          <Radio-group v-model="person.category" type="button">
            <Radio label="1">灵活就业人员</Radio>
            <Radio label="2">公益性岗位</Radio>
            <Radio label="3">企业吸纳</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="申请条件" required>
          <Radio-group v-model="person.type" type="button">
            <Radio label="1">城镇零就业家庭的4050失业人员</Radio>
            <Radio label="2" v-if="person.category === '1' || person.category === '2'">抚养未成年子女单亲家庭成员的4050失业人员</Radio>
            <Radio label="3" v-if="person.category === '1' || person.category === '2'">享受城市居民最低生活保障的4050失业人员</Radio>
            <Radio label="4" v-if="person.category === '1' || person.category === '2'">持有《中华人民共和国残疾人证》的4050失业人员</Radio>
            <Radio label="7" v-if="person.category === '3'">4050失业人员且连续失业半年以上</Radio>
            <Radio label="8" v-if="person.category === '3'">抚养未成年子女单亲家庭成员的失业人员</Radio>
            <Radio label="9" v-if="person.category === '3'">享受城市居民最低生活保障的失业人员</Radio>
            <Radio label="10" v-if="person.category === '3'">持有《中华人民共和国残疾人证》的失业人员</Radio>
            <Radio label="5">特困家庭未就业的高校毕业生</Radio>
            <Radio label="6">城镇登记失业的成年后孤儿</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="家庭成员" required>
          <Button shape="circle" type="info"  @click="openPerson">{{person.name === '' ? '申请人' : person.name}}</Button>
          <Button shape="circle" type="success" v-if="person.type === '1' && person.state === '2'" @click="openSpouse">{{spouse.name === '' ? '配偶' : spouse.name}}</Button>
          <Button shape="circle" type="warning" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)" @click="openChild1">{{child1.name === '' ? '子女1' : child1.name}}</Button>
          <Button shape="circle" type="warning" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)" @click="openChild2">{{child2.name === '' ? '子女2' : child2.name}}</Button>
          <Button shape="circle" type="warning" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)" @click="openChild3">{{child3.name === '' ? '子女3' : child3.name}}</Button>
          <Button shape="circle" type="warning" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && (person.childNum == 4 || person.childNum == 5)" @click="openChild4">{{child4.name === '' ? '子女4' : child4.name}}</Button>
          <Button shape="circle" type="warning" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.childNum == 5" @click="openChild5">{{child5.name === '' ? '子女5' : child5.name}}</Button>
        </Form-item>
        <Form-item label="留存材料" required>
          <Tag color="blue">{{person.name === '' ? '本人' : person.name}}-身份证复印件一份</Tag>
          <Tag color="blue">{{person.name === '' ? '本人' : person.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="blue">{{person.name === '' ? '本人' : person.name}}-城镇登记失业证明一份</Tag>
          <Tag color="blue" v-if="person.type === '3' || person.type === '9'">{{person.name === '' ? '本人' : person.name}}-《城市居民最低生活保障证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '3' || person.type === '9'">{{person.name === '' ? '本人' : person.name}}-《城市居民最低生活保障证》登记中的家庭成员户口簿复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '4'  || person.type === '10'">{{person.name === '' ? '本人' : person.name}}-《中华人民共和国残疾人证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '5'">{{person.name === '' ? '本人' : person.name}}-《城市居民最低生活保障证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '5'">{{person.name === '' ? '本人' : person.name}}-《济南市特困职工优待证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '5'">{{person.name === '' ? '本人' : person.name}}-《毕业证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '5'">{{person.name === '' ? '本人' : person.name}}-《报到证》复印件一份</Tag>
          <Tag color="blue" v-if="person.type === '6'">{{person.name === '' ? '本人' : person.name}}-县级以上民政部门认定的孤儿证明复印件一份</Tag>
          <Tag color="blue" v-if="person.state === '1'">{{person.name === '' ? '本人' : person.name}}-单身证明</Tag>
          <Tag color="green" v-if="person.type === '1' && person.state === '2'">{{spouse.name === '' ? '配偶' : spouse.name}}-身份证复印件一份</Tag>
          <Tag color="green" v-if="person.type === '1' && person.state === '2'">{{spouse.name === '' ? '配偶' : spouse.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="green" v-if="person.type === '1' && person.state === '2' && spouse.state === '6'">{{spouse.name === '' ? '配偶' : spouse.name}}-城镇登记失业证明一份</Tag>
          <Tag color="green" v-if="person.type === '1' && person.state === '2' && spouse.state === '7'">{{spouse.name === '' ? '配偶' : spouse.name}}-退休证复印件一份</Tag>
          <Tag color="green" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '3'">{{person.name === '' ? '本人' : person.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="green" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '4'">{{spouse.name === '' ? '配偶' : spouse.name}}-死亡证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '1' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-抚养证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '5' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '5' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '5' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-在校证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '2' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '2' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '2' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-结婚证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '1' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '1' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '1' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-城镇登记失业证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '3' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '3' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '3' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '4' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '4' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child1.state === '4' && (person.childNum == 1 || person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child1.name === '' ? '子女一' : child1.name}}-配偶死亡证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '1' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-抚养证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '5' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '5' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '5' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-在校证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '2' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '2' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '2' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-结婚证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '1' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '1' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '1' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-城镇登记失业证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '3' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '3' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '3' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '4' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '4' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child2.state === '4' && (person.childNum == 2 || person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child2.name === '' ? '子女二' : child2.name}}-配偶死亡证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '1' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-抚养证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '5' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '5' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '5' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-在校证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '2' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '2' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '2' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-结婚证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '1' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '1' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '1' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-城镇登记失业证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '3' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '3' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '3' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '4' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '4' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child3.state === '4' && (person.childNum == 3 || person.childNum == 4 || person.childNum == 5)">{{child3.name === '' ? '子女三' : child3.name}}-配偶死亡证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '1' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-抚养证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '5' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '5' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '5' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-在校证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '2' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '2' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '2' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-结婚证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '1' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '1' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '1' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-城镇登记失业证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '3' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '3' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '3' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '4' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '4' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child4.state === '4' && (person.childNum == 4 || person.childNum == 5)">{{child4.name === '' ? '子女四' : child4.name}}-配偶死亡证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && person.state === '1' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-抚养证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '5' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '5' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '5' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-在校证明</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '2' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '2' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '2' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-结婚证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '1' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '1' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '1' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-城镇登记失业证明一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '3' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '3' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '3' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-离婚证复印件一份或离婚协议书复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '4' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-身份证复印件一份</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '4' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-户口本复印件一份（户主页、索引页、个人单页）</Tag>
          <Tag color="yellow" v-if="(person.type === '1' || person.type === '2' || person.type === '8') && child5.state === '4' && person.childNum == 5">{{child5.name === '' ? '子女五' : child5.name}}-配偶死亡证明一份</Tag>
        </Form-item>
        <Form-item>
          <Button type="error" @click="gorReply">返回列表</Button>
          <Button type="info" @click="goReset">重置信息</Button>
          <Button type="success" @click="goSave">确认新增</Button>
        </Form-item>
      </Form>
    </div>
    <div class="layout-copy">
      2017 &copy; WangYiXin
    </div>
    <Modal v-model="person.modal" title="申请人的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700" :scrollable="true">
      <Form :model="persons" :rules="person_validate" :label-width="100">
        <Row>
          <Col span="12">
          <Form-item label="姓名" prop="name">
            <Input v-model="persons.name" :placeholder="persons.name === '' ? '请输入申请人的姓名' : '请输入' + persons.name + '的姓名'"></Input>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item label="证件号码" prop="number">
            <Input v-model="persons.number" :placeholder="persons.name === '' ? '请输入申请人的证件号码' : '请输入' + persons.name + '的证件号码'"></Input>
          </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <Form-item label="联系电话" prop="phone">
            <Input v-model="persons.phone" :placeholder="persons.name === '' ? '请输入申请人的联系电话' : '请输入' + persons.name + '的联系电话'"></Input>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item label="联系地址" prop="address">
            <Input v-model="persons.address" :placeholder="persons.name === '' ? '请输入申请人的联系地址' : '请输入' + persons.name + '的联系地址'"></Input>
          </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <Form-item label="原工作单位" prop="company">
            <Input v-model="persons.company" :placeholder="persons.name === '' ? '请输入申请人的失业前所在单位' : '请输入' + persons.name + '的失业前所在单位'"></Input>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item label="剩余享受月数" required>
            <Slider v-model="persons.remainMonth" max="60" show-stops show-tip show-input></Slider>
          </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <Form-item label="失业时间" prop="timeA">
            <Date-picker type="date" v-model="persons.timeA" :placeholder="persons.name === '' ? '请选择申请人的失业时间' : '请输入' + persons.name + '的失业时间'"></Date-picker>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item label="登记时间" prop="timeB">
            <Date-picker type="date" v-model="persons.timeB" :placeholder="persons.name === '' ? '请选择申请人的城镇登记失业时间' : '请输入' + persons.name + '的城镇登记失业时间'"></Date-picker>
          </Form-item>
          </Col>
        </Row>
        <Row v-if="person.type === '1' || person.type === '2' || person.type === '8'">
          <Col span="12">
          <Form-item label="婚姻状况"  required>
            <Radio-group v-model="persons.state" type="button">
              <Radio label="1" v-if="person.type === '1' || person.type === '2' || person.type === '8'">未婚</Radio>
              <Radio label="2" v-if="person.type === '1'">已婚</Radio>
              <Radio label="3" v-if="person.type === '1' || person.type === '2' || person.type === '8'">离异</Radio>
              <Radio label="4" v-if="person.type === '1' || person.type === '2' || person.type === '8'">丧偶</Radio>
            </Radio-group>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item label="子女数量" required>
            <Slider v-model="persons.childNum" max="5" show-stops show-tip show-input></Slider>
          </Form-item>
          </Col>
        </Row>
        <Row v-if="person.category === '1'">
          <Col span="24">
          <Form-item label="从事岗位" required>
            <Radio-group v-model="persons.job" type="button">
              <Radio label="1">钟点工</Radio>
              <Radio label="2">早市、夜市摆摊经营</Radio>
              <Radio label="3">街道、社区组织的帮扶</Radio>
              <Radio label="4">家庭服务</Radio>
              <Radio label="5">家庭手工业</Radio>
              <Radio label="6">社区公益性劳动</Radio>
              <Radio label="7">其他就业帮扶</Radio>
            </Radio-group>
          </Form-item>
          </Col>
        </Row>
        <Row v-if="person.type === '4' || person.type === '10'">
          <Col span="24">
          <Form-item label="残疾证号" required>
            <Input v-model="persons.disability" placeholder="请享受人员残疾证号"></Input>
          </Form-item>
          </Col>
        </Row>
        <Row v-if="person.type === '3' || person.type === '9'">
          <Col span="24">
          <Form-item label="低保证号" required>
            <Input v-model="persons.security" placeholder="请享受人员低保证号"></Input>
          </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="24">
          <Form-item label="备注">
            <Input type="textarea" :rows="3" v-model="persons.remark" :placeholder="persons.name === '' ? '请输本人的备注' : '请输入' + persons.name + '的备注'"></Input>
          </Form-item>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button type="error" @click="person.modal = false">关闭</Button>
        <Button type="info" @click="resetPerson">重置</Button>
        <Button type="success" @click="savePerson">保存</Button>
      </div>
    </Modal>
    <Modal v-model="spouse.modal" title="配偶的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="spouses" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="spouses.state" type="button">
            <Radio label="6">未退休</Radio>
            <Radio label="7">已退休</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="spouses.name" :placeholder="spouses.name === '' ? '请输入配偶的姓名' : '请输入' + spouses.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="spouses.number" :placeholder="spouses.name === '' ? '请输入配偶的证件号码' : '请输入' + spouses.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="spouses.company" :placeholder="spouses.name === '' ? '请输入配偶的原（现）工作（学习）单位' : '请输入' + spouses.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="spouses.time" :placeholder="spouses.name === '' ? '请选择配偶的城镇登记失业时间' : '请选择' + spouses.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="spouses.remark" :placeholder="spouses.name === '' ? '请输入配偶的备注' : '请输入' + spouses.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="spouse.modal = false">关闭</Button>
        <Button type="info" @click="resetSpouse">重置</Button>
        <Button type="success" @click="saveSpouse">保存</Button>
      </div>
    </Modal>
    <Modal v-model="child1.modal" title="子女1的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="child1s" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="child1s.state" type="button">
            <Radio label="1">已婚</Radio>
            <Radio label="2">未婚</Radio>
            <Radio label="3">离异</Radio>
            <Radio label="4">丧偶</Radio>
            <Radio label="5">在校学生</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="child1s.name" :placeholder="child1s.name === '' ? '请输入子女1的姓名' : '请输入' + child1s.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="child1s.number" :placeholder="child1s.name === '' ? '请输入子女1的证件号码' : '请输入' + child1s.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="child1s.company" :placeholder="child1s.name === '' ? '请输入子女1的原（现）工作（学习）单位' : '请输入' + child1s.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="child1s.time" :placeholder="child1s.name === '' ? '请选择子女1的城镇登记失业时间' : '请选择' + child1s.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="child1s.remark" :placeholder="child1s.name === '' ? '请输入子女1的备注' : '请输入' + child1s.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="child1.modal = false">关闭</Button>
        <Button type="info" @click="resetChild1">重置</Button>
        <Button type="success" @click="saveChild1">保存</Button>
      </div>
    </Modal>
    <Modal v-model="child2.modal" title="子女2的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="child2s" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="child2s.state" type="button">
            <Radio label="1">已婚</Radio>
            <Radio label="2">未婚</Radio>
            <Radio label="3">离异</Radio>
            <Radio label="4">丧偶</Radio>
            <Radio label="5">在校学生</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="child2s.name" :placeholder="child2s.name === '' ? '请输入子女2的姓名' : '请输入' + child2s.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="child2s.number" :placeholder="child2s.name === '' ? '请输入子女2的证件号码' : '请输入' + child2s.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="child2s.company" :placeholder="child2s.name === '' ? '请输入子女2的原（现）工作（学习）单位' : '请输入' + child2s.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="child2s.time" :placeholder="child2s.name === '' ? '请选择子女2的城镇登记失业时间' : '请选择' + child2s.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="child2s.remark" :placeholder="child2s.name === '' ? '请输入子女2的备注' : '请输入' + child2s.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="child2.modal = false">关闭</Button>
        <Button type="info" @click="resetChild2">重置</Button>
        <Button type="success" @click="saveChild2">保存</Button>
      </div>
    </Modal>
    <Modal v-model="child3.modal" title="子女3的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="child3" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="child3s.state" type="button">
            <Radio label="1">已婚</Radio>
            <Radio label="2">未婚</Radio>
            <Radio label="3">离异</Radio>
            <Radio label="4">丧偶</Radio>
            <Radio label="5">在校学生</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="child3s.name" :placeholder="child3s.name === '' ? '请输入子女3的姓名' : '请输入' + child3s.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="child3s.number" :placeholder="child3s.name === '' ? '请输入子女3的证件号码' : '请输入' + child3s.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="child3s.company" :placeholder="child3s.name === '' ? '请输入子女3的原（现）工作（学习）单位' : '请输入' + child3s.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="child3s.time" :placeholder="child3s.name === '' ? '请选择子女3的城镇登记失业时间' : '请选择' + child3s.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="child3s.remark" :placeholder="child3s.name === '' ? '请输入子女3的备注' : '请输入' + child3s.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="child3.modal = false">关闭</Button>
        <Button type="info" @click="resetChild3">重置</Button>
        <Button type="success" @click="saveChild3">保存</Button>
      </div>
    </Modal>
    <Modal v-model="child4.modal" title="子女4的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="child4s" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="child4s.state" type="button">
            <Radio label="1">已婚</Radio>
            <Radio label="2">未婚</Radio>
            <Radio label="3">离异</Radio>
            <Radio label="4">丧偶</Radio>
            <Radio label="5">在校学生</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="child4s.name" :placeholder="child4s.name === '' ? '请输入子女4的姓名' : '请输入' + child4s.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="child4s.number" :placeholder="child4s.name === '' ? '请输入子女4的证件号码' : '请输入' + child4s.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="child4s.company" :placeholder="child4s.name === '' ? '请输入子女4的原（现）工作（学习）单位' : '请输入' + child4s.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="child4s.time" :placeholder="child4s.name === '' ? '请选择子女4的城镇登记失业时间' : '请选择' + child4s.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="child4s.remark" :placeholder="child4s.name === '' ? '请输入子女4的备注' : '请输入' + child4s.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="child4.modal = false">关闭</Button>
        <Button type="info" @click="resetChild4">重置</Button>
        <Button type="success" @click="saveChild4">保存</Button>
      </div>
    </Modal>
    <Modal v-model="child5.modal" title="子女5的信息" :styles="{top: '20px'}" :closable="false" :mask-closable="false" :width="700">
      <Form :model="child5s" :rules="family_validate" :label-width="100">
        <Form-item label="状态" prop="state">
          <Radio-group v-model="child5s.state" type="button">
            <Radio label="1">已婚</Radio>
            <Radio label="2">未婚</Radio>
            <Radio label="3">离异</Radio>
            <Radio label="4">丧偶</Radio>
            <Radio label="5">在校学生</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input v-model="child5s.name" :placeholder="child5s.name === '' ? '请输入子女5的姓名' : '请输入' + child5s.name + '的姓名'"></Input>
        </Form-item>
        <Form-item label="证件号码" prop="number">
          <Input v-model="child5s.number" :placeholder="child5s.name === '' ? '请输入子女5的证件号码' : '请输入' + child5s.name + '的证件号码'"></Input>
        </Form-item>
        <Form-item label="工作单位" prop="company">
          <Input v-model="child5s.company" :placeholder="child5s.name === '' ? '请输入子女5的原（现）工作（学习）单位' : '请输入' + child5s.name + '的原（现）工作（学习）单位'"></Input>
        </Form-item>
        <Form-item label="登记时间" prop="time">
          <Date-picker type="date" v-model="child5s.time" :placeholder="child5s.name === '' ? '请选择子女5的城镇登记失业时间' : '请选择' + child5s.name + '的城镇登记失业时间'"></Date-picker>
        </Form-item>
        <Form-item label="备注">
          <Input type="textarea" :rows="3" v-model="child5s.remark" :placeholder="child5s.name === '' ? '请输入子女5的备注' : '请输入' + child5s.name + '的备注'"></Input>
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" @click="child5.modal = false">关闭</Button>
        <Button type="info" @click="resetChild5">重置</Button>
        <Button type="success" @click="saveChild5">保存</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'add',
    data () {
      const IDNumberCheck = (rule, value, callback) => {
        if (!value || !/^\d{17}(\d|X)$/i.test(value)) {
          callback(new Error('格式错误'))
        } else {
          value = value.split('')
          let factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
          let parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ]
          let sum = 0
          let ai = 0
          let wi = 0
          for (let i = 0; i < 17; i++) {
            ai = value[i]
            wi = factor[i]
            sum += ai * wi
          }
          if (parity[sum % 11].toString() !== value[17].toString()) {
            callback(new Error('校验码错误'))
          }
        }
      }
      return {
        person_validate: {
          name: [
            { required: true, message: '姓名应为2到6个汉字', trigger: 'change', pattern: /^[\u4e00-\u9fa5]{2,6}$/ }
          ],
          number: [
            { required: true, message: '证件号码不能为空', trigger: 'change' },
            { validator: IDNumberCheck, trigger: 'change' }
          ],
          phone: [
            { required: true, message: '联系电话不能为空', trigger: 'change' },
            { message: '请输入11位手机号码', trigger: 'change', pattern: /^1(3|4|5|7|8)\d{9}$/ }
          ],
          address: [
            { required: true, message: '联系地址不能为空', trigger: 'change' },
            { min: 2, message: '联系地址不能少于2个字符', trigger: 'change' }
          ],
          company: [
            { required: true, message: '原工作单位不能为空', trigger: 'change' },
            { min: 2, message: '原工作单位不能少于2个字符', trigger: 'change' }
          ],
          timeA: [
            { required: true, type: 'date', message: '请选择失业时间', trigger: 'change' }
          ],
          timeB: [
            { required: true, type: 'date', message: '请选择城镇登记失业时间', trigger: 'change' }
          ]
        },
        family_validate: {
          name: [
            { required: true, message: '姓名应为2到6个汉字', trigger: 'change', pattern: /^[\u4e00-\u9fa5]{2,6}$/ }
          ],
          number: [
            { required: true, message: '证件号码不能为空', trigger: 'change' },
            { validator: IDNumberCheck, trigger: 'change' }
          ],
          company: [
            { required: true, message: '原工作单位不能为空', trigger: 'change' },
            { min: 2, message: '原工作单位不能少于2个字符', trigger: 'change' }
          ],
          time: [
            { required: true, type: 'date', message: '请选择城镇登记失业时间', trigger: 'change' }
          ],
          state: [
            { required: true, message: '请选择状态', trigger: 'change' }
          ]
        },
        state: '',
        childNum: 0,
        person: {
          category: '1',
          type: '1',
          state: '',
          modal: false,
          name: '',
          number: '',
          phone: '',
          address: '',
          company: '',
          timeA: '',
          timeB: '',
          job: '',
          disability: '',
          security: '',
          remark: '',
          childNum: 0,
          remainMonth: 36
        },
        child1: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child2: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child3: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child4: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child5: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        spouse: {
          modal: false,
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        persons: {
          state: '',
          name: '',
          number: '',
          phone: '',
          address: '',
          company: '',
          timeA: '',
          timeB: '',
          job: '',
          disability: '',
          security: '',
          remark: '',
          childNum: 0,
          remainMonth: 36
        },
        child1s: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child2s: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child3s: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child4s: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        child5s: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        },
        spouses: {
          state: '',
          name: '',
          number: '',
          company: '',
          time: '',
          remark: ''
        }
      }
    },
    watch: {
      state: function (val) {
        this.resetSpouse()
      },
      childMun: function (val) {
        this.resetChild1()
        this.resetChild2()
        this.resetChild3()
        this.resetChild4()
        this.resetChild5()
      }
    },
    methods: {
      gorReply () {
        this.$router.push({ path: '/list' })
      },
      savePerson () {
        if (this.persons.name === '' ||
          this.persons.number === '' ||
          this.persons.phone === '' ||
          this.persons.address === '' ||
          this.persons.company === '' ||
          this.persons.timeA === '' ||
          this.persons.timeB === '' ||
          this.persons.job === '' ||
          this.persons.state === '' ||
          (this.person.type === '4' && this.persons.disability === '') ||
          (this.person.type === '10' && this.persons.disability === '') ||
          (this.person.type === '3' && this.persons.security === '') ||
          (this.person.type === '9' && this.persons.security === '') ||
          (this.person.type === '1' && this.persons.state === '') ||
          (this.person.type === '1' && this.persons.job === '') ||
          (this.person.type === '2' && this.persons.state === '') ||
          (this.person.type === '2' && this.persons.job === '') ||
          (this.person.type === '3' && this.persons.state !== '') ||
          (this.person.type === '3' && this.persons.job === '') ||
          (this.person.type === '4' && this.persons.state !== '') ||
          (this.person.type === '4' && this.persons.job === '') ||
          (this.person.type === '5' && this.persons.state !== '') ||
          (this.person.type === '5' && this.persons.job === '') ||
          (this.person.type === '6' && this.persons.state !== '') ||
          (this.person.type === '6' && this.persons.job === '') ||
          (this.person.type === '7' && this.persons.state !== '') ||
          (this.person.type === '7' && this.persons.job !== '') ||
          (this.person.type === '8' && this.persons.state === '') ||
          (this.person.type === '8' && this.persons.job === '') ||
          (this.person.type === '9' && this.persons.state !== '') ||
          (this.person.type === '9' && this.persons.job !== '') ||
          (this.person.type === '10' && this.persons.state !== '') ||
          (this.person.type === '10' && this.persons.job !== '')
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.person.name = this.persons.name
          this.person.number = this.persons.number
          this.person.phone = this.persons.phone
          this.person.address = this.persons.address
          this.person.company = this.persons.company
          this.person.timeA = this.persons.timeA
          this.person.timeB = this.persons.timeB
          this.person.job = this.persons.job
          this.person.disability = this.persons.disability
          this.person.security = this.persons.security
          this.person.remark = this.persons.remark
          this.person.state = this.persons.state
          this.person.childNum = this.persons.childNum
          this.state = this.persons.state
          this.childNum = this.persons.childNum
          this.person.modal = false
        }
      },
      saveSpouse () {
        if (this.spouses.name === '' ||
          this.spouses.number === '' ||
          this.spouses.time === '' ||
          this.spouses.state === '' ||
          this.spouses.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.spouse.name = this.spouses.name
          this.spouse.number = this.spouses.number
          this.spouse.time = this.spouses.time
          this.spouse.state = this.spouses.state
          this.spouse.company = this.spouses.company
          this.spouse.modal = false
        }
      },
      saveChild1 () {
        if (this.child1.name === '' ||
          this.child1.number === '' ||
          this.child1.time === '' ||
          this.child1.state === '' ||
          this.child1.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.child1.name = this.child1s.name
          this.child1.number = this.child1s.number
          this.child1.time = this.child1s.time
          this.child1.state = this.child1s.state
          this.child1.company = this.child1s.company
          this.child1.modal = false
        }
      },
      saveChild2 () {
        if (this.child2.name === '' ||
          this.child2.number === '' ||
          this.child2.time === '' ||
          this.child2.state === '' ||
          this.child2.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.child2.name = this.child2s.name
          this.child2.number = this.child2s.number
          this.child2.time = this.child2s.time
          this.child2.state = this.child2s.state
          this.child2.company = this.child2s.company
          this.child2.modal = false
        }
      },
      saveChild3 () {
        if (this.child3.name === '' ||
          this.child3.number === '' ||
          this.child3.time === '' ||
          this.child3.state === '' ||
          this.child3.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.child3.name = this.child3s.name
          this.child3.number = this.child3s.number
          this.child3.time = this.child3s.time
          this.child3.state = this.child3s.state
          this.child3.company = this.child3s.company
          this.child3.modal = false
        }
      },
      saveChild4 () {
        if (this.child4.name === '' ||
          this.child4.number === '' ||
          this.child4.time === '' ||
          this.child4.state === '' ||
          this.child4.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.child4.name = this.child4s.name
          this.child4.number = this.child4s.number
          this.child4.time = this.child4s.time
          this.child4.state = this.child4s.state
          this.child4.company = this.child4s.company
          this.child4.modal = false
        }
      },
      saveChild5 () {
        if (this.child5.name === '' ||
          this.child5.number === '' ||
          this.child5.time === '' ||
          this.child5.state === '' ||
          this.child5.company === ''
        ) {
          this.$Notice.error({
            title: '有部分信息未填写!'
          })
        } else {
          this.child5.name = this.child5s.name
          this.child5.number = this.child5s.number
          this.child5.time = this.child5s.time
          this.child5.state = this.child5s.state
          this.child5.company = this.child5s.company
          this.child5.modal = false
        }
      },
      goReset () {
        this.person.category = '1'
        this.person.type = '1'
        this.resetPerson()
        this.resetSpouse()
        this.resetChild1()
        this.resetChild2()
        this.resetChild3()
        this.resetChild4()
        this.resetChild5()
      },
      resetPerson () {
        this.persons.name = ''
        this.persons.number = ''
        this.persons.phone = ''
        this.persons.address = ''
        this.persons.company = ''
        this.persons.timeA = ''
        this.persons.timeB = ''
        this.persons.job = ''
        this.persons.state = ''
        this.persons.remark = ''
        this.persons.childNum = 0
        this.persons.disability = ''
        this.persons.security = ''
        this.persons.remainMonth = 36
      },
      resetSpouse () {
        this.spouses.state = ''
        this.spouses.name = ''
        this.spouses.number = ''
        this.spouses.company = ''
        this.spouses.time = ''
        this.spouses.remark = ''
      },
      resetChild1 () {
        this.child1s.state = ''
        this.child1s.name = ''
        this.child1s.number = ''
        this.child1s.company = ''
        this.child1s.time = ''
        this.child1s.remark = ''
      },
      resetChild2 () {
        this.child2s.state = ''
        this.child2s.name = ''
        this.child2s.number = ''
        this.child2s.company = ''
        this.child2s.time = ''
        this.child2s.remark = ''
      },
      resetChild3 () {
        this.child3s.state = ''
        this.child3s.name = ''
        this.child3s.number = ''
        this.child3s.company = ''
        this.child3s.time = ''
        this.child3s.remark = ''
      },
      resetChild4 () {
        this.child4s.state = ''
        this.child4s.name = ''
        this.child4s.number = ''
        this.child4s.company = ''
        this.child4s.time = ''
        this.child4s.remark = ''
      },
      resetChild5 () {
        this.child5s.state = ''
        this.child5s.name = ''
        this.child5s.number = ''
        this.child5s.company = ''
        this.child5s.time = ''
        this.child5s.remark = ''
      },
      openPerson () {
        this.persons.name = this.person.name
        this.persons.number = this.person.number
        this.persons.phone = this.person.phone
        this.persons.address = this.person.address
        this.persons.company = this.person.company
        this.persons.timeA = this.person.timeA
        this.persons.timeB = this.person.timeB
        this.persons.job = this.person.job
        this.persons.disability = this.person.disability
        this.persons.security = this.person.security
        this.persons.remark = this.person.remark
        this.persons.state = this.person.state
        this.persons.childNum = this.person.childNum
        this.person.modal = true
      },
      openSpouse () {
        this.spouses.name = this.spouse.name
        this.spouses.number = this.spouse.number
        this.spouses.time = this.spouse.time
        this.spouses.state = this.spouse.state
        this.spouses.company = this.spouse.company
        this.spouse.modal = true
      },
      openChild1 () {
        this.child1s.name = this.child1.name
        this.child1s.number = this.child1.number
        this.child1s.time = this.child1.time
        this.child1s.state = this.child1.state
        this.child1s.company = this.child1.company
        this.child1.modal = true
      },
      openChild2 () {
        this.child2s.name = this.child2.name
        this.child2s.number = this.child2.number
        this.child2s.time = this.child2.time
        this.child2s.state = this.child2.state
        this.child2s.company = this.child2.company
        this.child2.modal = true
      },
      openChild3 () {
        this.child3s.name = this.child3.name
        this.child3s.number = this.child3.number
        this.child3s.time = this.child3.time
        this.child3s.state = this.child3.state
        this.child3s.company = this.child3.company
        this.child3.modal = true
      },
      openChild4 () {
        this.child4s.name = this.child4.name
        this.child4s.number = this.child4.number
        this.child4s.time = this.child4.time
        this.child4s.state = this.child4.state
        this.child4s.company = this.child4.company
        this.child4.modal = true
      },
      openChild5 () {
        this.child5s.name = this.child5.name
        this.child5s.number = this.child5.number
        this.child5s.time = this.child5.time
        this.child5s.state = this.child5.state
        this.child5s.company = this.child5.company
        this.child5.modal = true
      },
      goSave () {
        if (this.persons.timeA.toString() !== '') {
          this.person.timeA = this.persons.timeA.getTime().toString()
        }
        if (this.persons.timeB.toString() !== '') {
          this.person.timeB = this.persons.timeB.getTime().toString()
        }
        if (this.child1s.time.toString() !== '') {
          this.child1.time = this.child1s.time.getTime().toString()
        }
        if (this.child2s.time.toString() !== '') {
          this.child2.time = this.child2s.time.getTime().toString()
        }
        if (this.child3s.time.toString() !== '') {
          this.child3.time = this.child3s.time.getTime().toString()
        }
        if (this.child4s.time.toString() !== '') {
          this.child4.time = this.child4s.time.getTime().toString()
        }
        if (this.child5s.time.toString() !== '') {
          this.child5.time = this.child5s.time.getTime().toString()
        }
        if (this.spouses.time.toString() !== '') {
          this.spouse.time = this.spouses.time.getTime().toString()
        }
        this.$http.get(
          API.save,
          { params: {
            person: this.person,
            spouse: this.spouse,
            child1: this.child1,
            child2: this.child2,
            child3: this.child3,
            child4: this.child4,
            child5: this.child5
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.$Notice.error({
              title: '请重新登录!'
            })
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.$Notice.success({
              title: '保存成功!'
            })
            setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
          } else {
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      }
    }
  }
</script>
<style scoped>
    .layout{
        border: 1px solid #d7dde4;
        background: #f5f7f9;
    }
    .layout-logo{
        width: 100px;
        height: 30px;
        background: #5b6270;
        border-radius: 3px;
        float: left;
        position: relative;
        top: 15px;
        left: 20px;
    }
    .layout-nav{
        width: 420px;
        margin: 0 auto;
    }
    .layout-assistant{
        width: 300px;
        margin: 0 auto;
        height: inherit;
    }
    .layout-breadcrumb{
        padding: 10px 15px 0;
    }
    .layout-content{
        min-height: 200px;
        margin: 15px;
        overflow: hidden;
        background: #fff;
        border-radius: 4px;
    }
    .layout-content-main{
        padding: 10px;
    }
    .layout-copy{
        text-align: center;
        padding: 10px 0 20px;
        color: #9ea7b4;
    }
</style>
