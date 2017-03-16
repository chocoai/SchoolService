var path = require('path')
var utils = require('./utils')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var CopyWebpackPlugin = require('copy-webpack-plugin')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin')

var env = config.build.env

var webpackConfig = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({
      sourceMap: config.build.productionSourceMap,
      extract: true
    })
  },
  devtool: config.build.productionSourceMap ? '#source-map' : false,
  output: {
    path: config.build.assetsRoot,
    filename: utils.assetsPath('js/[name].[chunkhash].js'),
    chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
  },
  plugins: [
    // http://vuejs.github.io/vue-loader/en/workflow/production.html
    new webpack.DefinePlugin({
      'process.env': env
    }),
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false
      },
      sourceMap: true
    }),
    // extract css into its own file
    new ExtractTextPlugin({
      filename: utils.assetsPath('css/[name].[contenthash].css')
    }),
    // Compress extracted CSS. We are using this plugin so that possible
    // duplicated CSS from different components can be deduped.
    new OptimizeCSSPlugin(),
    // generate dist index.html with correct asset hash for caching.
    // you can customize output by editing /index.html
    // see https://github.com/ampedandwired/html-webpack-plugin
    new HtmlWebpackPlugin({
      title: '移动_登录',
      filename: '../dist/static/html/mobile/Mobile_Login.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Login'],
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
      title: '移动_管理_主页',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Home.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Home'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_学期',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Semester.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Semester'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_课程',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Course.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Course'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_学生',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Student.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Student'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_家长',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Parent.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Parent'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_教师',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Teacher.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Teacher'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_教师消息',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_TeacherMessage.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_TeacherMessage'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_管理_班级',
      filename: '../dist/static/html/mobile/manager/Mobile_Manager_Room.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Manager_Room'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_教师_主页',
      filename: '../dist/static/html/mobile/teacher/Mobile_Teacher_Home.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Teacher_Home'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_教师_课程',
      filename: '../dist/static/html/mobile/teacher/Mobile_Teacher_Course.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Teacher_Course'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_教师_教师消息',
      filename: '../dist/static/html/mobile/manager/Mobile_Teacher_TeacherMessage.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Teacher_TeacherMessage'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_教师_学生',
      filename: '../dist/static/html/mobile/manager/Mobile_Teacher_Student.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Teacher_Student'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_家长_主页',
      filename: '../dist/static/html/mobile/manager/Mobile_Parent_Home.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Parent_Home'],
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: true
      },
      chunksSortMode: 'dependency'
    }),
    new HtmlWebpackPlugin({
      title: '移动_家长_学生',
      filename: '../dist/static/html/mobile/manager/Mobile_Parent_Student.html',
      template: 'html/template.html',
      inject: true,
      chunks: ['vendor', 'manifest', 'Mobile_Parent_Student'],
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
    // copy custom static assets
    new CopyWebpackPlugin([
      {
        from: path.resolve(__dirname, '../dist'),
        to: path.resolve(__dirname, '../static'),
        force: true,
        toType: 'dir',
        ignore: ['.*']
      }
    ]),

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

if (config.build.bundleAnalyzerReport) {
  var BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
  webpackConfig.plugins.push(new BundleAnalyzerPlugin())
}

module.exports = webpackConfig
