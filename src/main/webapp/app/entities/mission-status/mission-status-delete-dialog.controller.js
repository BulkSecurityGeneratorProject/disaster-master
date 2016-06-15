(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionStatusDeleteController',MissionStatusDeleteController);

    MissionStatusDeleteController.$inject = ['$uibModalInstance', 'entity', 'MissionStatus'];

    function MissionStatusDeleteController($uibModalInstance, entity, MissionStatus) {
        var vm = this;

        vm.missionStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MissionStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
