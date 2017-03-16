var path = require('path')
var utils = require('./utils')
var config = require('../config')
var vueLoaderConfig = require('./vue-loader.conf')

function resolve (dir) {
  return path.join(__dirname, '..', dir)
}

module.exports = {
  entry: {
    Mobile_Login: './src/Mobile/Login/Index.js',
    Mobile_Manager_Home: './src/Mobile/Manager/Home/Index.js',
    Mobile_Manager_Semester: './src/Mobile/Manager/Semester/Index.js',
    Mobile_Manager_Course: './src/Mobile/Manager/Course/Index.js',
    Mobile_Manager_Teacher: './src/Mobile/Manager/Teacher/Index.js',
    Mobile_Manager_TeacherMessage: './src/Mobile/Manager/TeacherMessage/Index.js',
    Mobile_Manager_Room: './src/Mobile/Manager/Room/Index.js',
    Mobile_Manager_Student: './src/Mobile/Manager/Student/Index.js',
    Mobile_Manager_Parent: './src/Mobile/Manager/Parent/Index.js',
    Mobile_Teacher_Home: './src/Mobile/Teacher/Home/Index.js',
    Mobile_Teacher_Course: './src/Mobile/Teacher/Course/Index.js',
    Mobile_Teacher_TeacherMessage: './src/Mobile/Teacher/TeacherMessage/Index.js',
    Mobile_Teacher_Student: './src/Mobile/Teacher/Student/Index.js',
    Mobile_Parent_Home: './src/Mobile/Parent/Home/Index.js',
    Mobile_Parent_Student: './src/Mobile/Parent/Student/Index.js'
  },
  output: {
    path: config.build.assetsRoot,
    filename: '[name].js',
    publicPath: process.env.NODE_ENV === 'production'
      ? config.build.assetsPublicPath
      : config.dev.assetsPublicPath
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue': 'vue/dist/vue.esm.js',
      '@': resolve('src'),
      'assets': resolve('src\\assets'),
      'muse-components': 'muse-ui/src',
    }
  },
  module: {
    rules: [
      {
        test: /\.(js|vue)$/,
        loader: 'eslint-loader',
        enforce: "pre",
        include: [resolve('src'), resolve('test')],
        options: {
          formatter: require('eslint-friendly-formatter')
        }
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: vueLoaderConfig
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        include: [resolve('src'), resolve('test')]
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        query: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        query: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      },
      {
        test: /muse-ui.src.*?js$/,
        loader: 'babel-loader'
      }
    ]
  }
}
