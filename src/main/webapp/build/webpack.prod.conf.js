var path = require('path')
var config = require('../config')
var utils = require('./utils')
var webpack = require('webpack')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var env = config.build.env

var webpackConfig = merge(baseWebpackConfig, {
  module: {
    loaders: utils.styleLoaders({ sourceMap: config.build.productionSourceMap, extract: true })
  },
  devtool: config.build.productionSourceMap ? '#source-map' : false,
  output: {
    path: config.build.assetsRoot,
    filename: utils.assetsPath('js/[name].[chunkhash].js'),
    chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
  },
  vue: {
    loaders: utils.cssLoaders({
      sourceMap: config.build.productionSourceMap,
      extract: true
    })
  },
  plugins: [
    // http://vuejs.github.io/vue-loader/en/workflow/production.html
    new webpack.DefinePlugin({
      'process.env': env
    }),
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false
      }
    }),
    new webpack.optimize.OccurrenceOrderPlugin(),
    // extract css into its own file
    new ExtractTextPlugin(utils.assetsPath('css/[name].[contenthash].css')),
    // generate dist index.html with correct asset hash for caching.
    // you can customize output by editing /index.html
    // see https://github.com/ampedandwired/html-webpack-plugin
    // new HtmlWebpackPlugin({
    //   filename: config.build.index,
    //   template: 'html/template.html',
    //   inject: true,
    //   minify: {
    //     removeComments: true,
    //     collapseWhitespace: true,
    //     removeAttributeQuotes: true
    //     // more options:
    //     // https://github.com/kangax/html-minifier#options-quick-reference
    //   },
    //   // necessary to consistently work with multiple chunks via CommonsChunkPlugin
    //   chunksSortMode: 'dependency'
    // }),
    new HtmlWebpackPlugin({
      title: '登录',
      filename: '../dist/static/Login.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'login'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
        // more options:
        // https://github.com/kangax/html-minifier#options-quick-reference
      },
      // necessary to consistently work with multiple chunks via CommonsChunkPlugin
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '主页',
      filename: '../dist/static/HomeForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'homeForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
        // more options:
        // https://github.com/kangax/html-minifier#options-quick-reference
      },
      // necessary to consistently work with multiple chunks via CommonsChunkPlugin
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '教师管理',
      filename: '../dist/static/TeacherForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'teacherForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '班级管理',
      filename: '../dist/static/RoomForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'roomForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '社团管理',
      filename: '../dist/static/TeamForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'teamForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '公告管理',
      filename: '../dist/static/NoticeForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'noticeForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '家长管理',
      filename: '../dist/static/ParentForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'parentForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '学生管理',
      filename: '../dist/static/StudentForManager.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'studentForManager'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '主页',
      filename: '../dist/static/HomeForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'homeForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '公告管理',
      filename: '../dist/static/NoticeForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'noticeForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '学生管理',
      filename: '../dist/static/StudentOfRoomForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'studentOfRoomForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '学生管理',
      filename: '../dist/static/StudentOfTeamForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'studentOfTeamForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '班级消息',
      filename: '../dist/static/RoomworkForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'roomworkForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '社团消息',
      filename: '../dist/static/TeamworkForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'teamworkForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '用户信息',
      filename: '../dist/static/PersonalForTeacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'personalForTeacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '主页',
      filename: '../dist/static/HomeForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'homeForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '公告管理',
      filename: '../dist/static/NoticeForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'noticeForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '班级消息',
      filename: '../dist/static/RoomworkForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'roomworkForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '社团消息',
      filename: '../dist/static/TeamworkForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'teamworkForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '学生管理',
      filename: '../dist/static/StudentForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'studentForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '用户信息',
      filename: '../dist/static/PersonalForParent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'personalForParent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    // split vendor js into its own file
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      minChunks: function (module, count) {
        // any required modules inside node_modules are extracted to vendor
        return (
          module.resource &&
          /\.js$/.test(module.resource) &&
          module.resource.indexOf(
            path.join(__dirname, '../node_modules')
          ) === 0
        )
      }
    }),
    // extract webpack runtime and module manifest to its own file in order to
    // prevent vendor hash from being updated whenever app bundle is updated
    new webpack.optimize.CommonsChunkPlugin({
      name: 'manifest',
      chunks: ['vendor']
    }),
    new webpack.ProvidePlugin({
      '$': 'jquery',
      'vue': 'vue'
    })
  ]
})

if (config.build.productionGzip) {
  var CompressionWebpackPlugin = require('compression-webpack-plugin')

  webpackConfig.plugins.push(
    new CompressionWebpackPlugin({
      asset: '[path].gz[query]',
      algorithm: 'gzip',
      test: new RegExp(
        '\\.(' +
        config.build.productionGzipExtensions.join('|') +
        ')$'
      ),
      threshold: 10240,
      minRatio: 0.8
    })
  )
}

module.exports = webpackConfig
