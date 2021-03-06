/*
 *  Copyright (c) 2020. MobilityData IO.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import React from "react";

/**
 * A Dropzone component
 * @param onDrop {function}       method triggered when an element is dropped on the zone
 * @param accept {Boolean}        string specifying the type of file accepted
 * @param id {string}             DOM element id
 * @param useDropzone {function}  method triggered to define "drag and drop" behavior variables
 * @returns {JSX.Element}
 * @constructor
 */
const JsonDropzone = ({onDrop, accept, id, useDropzone}) => {
    const {getRootProps, getInputProps, isDragActive} = useDropzone({
        onDrop,
        accept,
    });

    return (
        <div {...getRootProps()}>
            <input id={id} className="dropzone-input" {...getInputProps()} />
            <div className="text-center">
                {isDragActive ? (
                    <p className="dropzone-content">Release to drop the files here</p>
                ) : (
                    <p className="dropzone-content">
                        Drag 'n' drop your .json configuration file
                    </p>
                )}
            </div>
        </div>
    );
};

export default JsonDropzone;
