import React from 'react';
import Header from './Header';
import LeagueMenu from './LeagueMenu';
import DepthBook from './DepthBook';
import OrderEntry from './OrderEntry';



class TraderDashboard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedInstrument: undefined
    };
  }


  handleInstrumentSelection = (selectedInstrument) => {
    this.setState({ selectedInstrument });
  }

  unselectContract  = () => this.setState({selectedInstrument:undefined});


  render = (props) => (
    <React.Fragment>
      <div className='row' style={{
        "height": "50px"
      }}>
        <Header />
      </div>
      <div className='row' style={{
        "height": "250px"
      }}>
        < div className='col divided' style={{
          "backgroundColor": "BlanchedAlmond",
          "flex":4
        }}>
          <LeagueMenu unselectContract={this.unselectContract} handleInstrumentSelection={this.handleInstrumentSelection} />

        </div>
        {this.state.selectedInstrument &&
                < div className='col divided' style={{
                  "flex": 1,
                  "backgroundColor":"CornSilk"
                }}>
                <DepthBook isin={this.state.selectedInstrument.getIsin()}/>
                </div>
         }
      </div>

      < div className='row' style={{
        "backgroundColor": "Moccasin"
      }}>
        {this.state.selectedInstrument &&
          <OrderEntry instrument={this.state.selectedInstrument} />
        }
      </ div>
    </React.Fragment>
  );

}

export default TraderDashboard;