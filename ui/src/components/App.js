import React from 'react';
import Header from './Header';
import LeagueMenu from './LeagueMenu';
import DepthBook from './DepthBook';

export const App = (props) => (
  <React.Fragment>
    <div className='row' style={{
      "height": "50px"
    }}>
      <Header />
    </div>
    <div className='row' style={{
      "height": "250px"
    }}>
      < div className='col' style={{
        "backgroundColor": "lightgrey"
      }}>
        <LeagueMenu />
      </div>
      < div className='col' style={{
        "backgroundColor": "gold"
      }}>
        Depth book here
      </div>
    </div>
  </React.Fragment>
);