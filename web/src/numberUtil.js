'use strict'
module.exports = {
  checkNumber: function (number) {
    let city = {
      11 : '北京',
      12 : '天津',
      13 : '河北',
      14 : '山西',
      15 : '内蒙古',
      21 : '辽宁',
      22 : '吉林',
      23 : '黑龙江',
      31 : '上海',
      32 : '江苏',
      33 : '浙江',
      34 : '安徽',
      35 : '福建',
      36 : '江西',
      37 : '山东',
      41 : '河南',
      42 : '湖北',
      43 : '湖南',
      44 : '广东',
      45 : '广西',
      46 : '海南',
      50 : '重庆',
      51 : '四川',
      52 : '贵州',
      53 : '云南',
      54 : '西藏',
      61 : '陕西',
      62 : '甘肃',
      63 : '青海',
      64 : '宁夏',
      65 : '新疆',
      71 : '台湾',
      81 : '香港',
      82 : '澳门',
      91 : '国外'
    };
    let pass= true;
    if (!number || !/^\d{17}(\d|X)$/i.test(number)) {
      pass = false
    } else if(!city[number.substr(0,2)]){
      pass = false;
    } else {
      number = number.split('')
      let factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
      let parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ]
      let sum = 0
      let ai = 0
      let wi = 0
      for (let i = 0; i < 17; i++) {
        ai = number[i]
        wi = factor[i]
        sum += ai * wi
      }
      if (parity[sum % 11].toString() !== number[17].toString()) {
        pass = false
      }
    }
    return pass
  }
}
