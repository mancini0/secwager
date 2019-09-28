import React from 'react';
import Header from './Header';
import ContractTable from './ContractTable';
import DepthBook from './DepthBook';

export const App = (props) => (
  <React.Fragment>
    <div className='row' style={{
      "height": "50px"
    }}>
      <Header/>
    </div>
    <div className='row' style={{
      "height": "250px"
    }}>
      < div className='column' style={{
        "background-color": "lightgrey"
      }}>
        <ContractTable contracts={[]}/>
      </div>
      < div className='column' style={{
        "background-color": "salmon"
      }}>
        depth book here
      </div>
    </div>
  </React.Fragment>
);