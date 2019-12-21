#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'fluwx_worker'
  s.version          = '0.0.1'
  s.summary          = '企业微信插件'
  s.description      = <<-DESC
企业微信插件
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/*'
  s.public_header_files = 'Classes/*.h' 
  s.static_framework = true
  s.dependency 'Flutter'

  # s.frameworks = ["SystemConfiguration", "CoreTelephony"]
  # s.libraries = ["z", "sqlite3.0", "c++"]
  s.preserve_paths = 'Lib/*.a'
  s.vendored_libraries = "**/*.a"

  # s.ios.deployment_target = '8.0'
end

