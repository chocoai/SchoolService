import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Button } from 'antd-mobile';

class TeacherManage extends Component {

  render() {
    return (
      <div>
        <Button>Start</Button>
      </div>
    );
  }
}
ReactDOM.render(<TeacherManage />, document.getElementById("TeacherManage"));