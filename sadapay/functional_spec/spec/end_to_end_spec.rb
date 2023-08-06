require 'spec_helper'

RSpec.describe 'End To End Suite' do
  describe "full scenarios" do
    it "input from file" do
      root_dir=File.join(File.dirname(__FILE__), '../..')
      fixtures=File.join(root_dir, 'functional_spec', 'fixtures')
      run_script=File.join(root_dir, 'bin/run.sh')
      expected=File.open(File.join(fixtures, 'file_output.txt')).read
      input_file=File.join(fixtures, 'file_input.txt')
      inventory_file=File.join(fixtures, 'inventory.csv')
      pty = PTY.spawn("#{run_script} #{inventory_file} #{input_file}")
      print 'Testing file input: '
#       expect(fetch_stdout(pty)).to eq(expected)
    end
  end
end