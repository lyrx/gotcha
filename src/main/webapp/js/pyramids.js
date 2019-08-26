
const main = (IPFS, ORBITDB) => {
    // If we're building with Webpack, use the injected IPFS module.
    // Otherwise use 'Ipfs' which is exposed by ipfs.min.js
    if (IPFS)
        Ipfs = IPFS

    // If we're building with Webpack, use the injected OrbitDB module.
    // Otherwise use 'OrbitDB' which is exposed by orbitdb.min.js
    if (ORBITDB)
        OrbitDB = ORBITDB


    // Create IPFS instance
    const ipfs = new Ipfs({
        repo: '/com/lyrx/pyramids/ipfs/0.0.1',
        start: true,
        preload: {
            enabled: false
        },
        EXPERIMENTAL: {
            pubsub: true,
        },
        config: {
            Addresses: {
                Swarm: [
                    // Use IPFS dev signal server
                    // '/dns4/star-signal.cloud.ipfs.team/wss/p2p-webrtc-star',
                    '/dns4/ws-star.discovery.libp2p.io/tcp/443/wss/p2p-websocket-star',
                    // Use local signal server
                    // '/ip4/0.0.0.0/tcp/9090/wss/p2p-webrtc-star',
                ]
            },
        }
    })



}



